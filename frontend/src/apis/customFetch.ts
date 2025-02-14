import { useTokenStore } from '@/stores/tokenStore';

const BASE_URL = import.meta.env.VITE_BASE_URL || 'http://localhost:8080';

interface CustomRequestInit extends RequestInit {
    params?: Record<string, string>;
}

/**
 * 최대 토큰 갱신 재시도 횟수.
 * 한 번 갱신 실패 시에는 더 이상 재시도하지 않고 로그인 페이지로 리다이렉션합니다.
 */
const MAX_REFRESH_ATTEMPTS = 1;

/**
 * 동시에 여러 요청에서 401 응답이 발생했을 때, refresh 요청이 중복으로 발생하지 않도록
 * 전역 변수로 Promise를 관리합니다.
 */
let refreshTokenPromise: Promise<Response> | null = null;

/**
 * API 요청을 위한 커스텀 fetch 함수.
 *
 * @param endpoint 요청 엔드포인트 (BASE_URL 이후 경로)
 * @param options fetch 옵션 및 추가 params
 * @param refreshAttempt 내부 재시도 카운트 (외부 호출 시 생략)
 * @returns fetch의 Response 객체
 */
export const customFetch = async (
    endpoint: string,
    options: CustomRequestInit = {},
    refreshAttempt = 0,
): Promise<Response> => {
    const { params, ...fetchOptions } = options;

    // URL 및 쿼리 파라미터 처리
    let url = `${BASE_URL}${endpoint}`;
    if (params) {
        const searchParams = new URLSearchParams(params);
        url += `?${searchParams.toString()}`;
    }

    // 기본 헤더 설정 (body가 FormData나 Blob이 아닐 때)
    const defaultHeaders: Record<string, string> = {};
    if (
        fetchOptions.body &&
        !(fetchOptions.body instanceof FormData) &&
        !(fetchOptions.body instanceof Blob)
    ) {
        defaultHeaders['Content-Type'] = 'application/json';
    }

    // Bearer 토큰 가져오기 (예시: localStorage)
    const accessToken = useTokenStore.getState().accessToken;
    if (accessToken) {
        defaultHeaders['Authorization'] = `Bearer ${accessToken}`;
    }

    const headers = { ...defaultHeaders, ...fetchOptions.headers };

    let response: Response;
    try {
        response = await fetch(url, {
            ...fetchOptions,
            headers,
            credentials: 'include',
        });
    } catch (error) {
        console.error('Network error during fetch:', error);
        throw new Error('Network error during API request');
    }

    // 응답이 정상적이지 않은 경우 처리
    if (!response.ok) {
        if (response.status === 401) {
            // 토큰 만료 등으로 인한 401 응답 처리
            if (refreshAttempt < MAX_REFRESH_ATTEMPTS) {
                try {
                    // refresh 요청이 이미 진행 중이면 해당 Promise를 재사용
                    if (!refreshTokenPromise) {
                        refreshTokenPromise = fetch(`${BASE_URL}/reissue`, {
                            credentials: 'include', // 서버에서 refresh token을 쿠키로 관리한다고 가정
                        });
                    }
                    let refreshResponse: Response;
                    try {
                        refreshResponse = await refreshTokenPromise;
                    } catch (refreshError) {
                        console.error(
                            'Error during token refresh:',
                            refreshError,
                        );
                        throw refreshError;
                    } finally {
                        // refreshTokenPromise는 항상 초기화
                        refreshTokenPromise = null;
                    }

                    if (refreshResponse.ok) {
                        // refresh 성공 시, 원래 요청 재시도 (재시도 횟수 증가)
                        return customFetch(
                            endpoint,
                            options,
                            refreshAttempt + 1,
                        );
                    } else {
                        console.error(
                            'Token refresh failed with status:',
                            refreshResponse.status,
                        );
                    }
                } catch (error) {
                    console.error('Exception during token refresh:', error);
                }
            }
            // refresh 시도 실패 또는 최대 재시도 횟수 초과 시 로그인 페이지로 이동
            // window.location.href = '/';
            throw new Error('Authentication failed. Redirecting to login.');
        }

        console.error('API request failed with status:', response.status);
        throw new Error('API request failed');
    }

    return response;
};
