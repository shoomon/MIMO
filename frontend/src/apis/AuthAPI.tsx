import { OAuthResponse, UserResponse } from '@/types/Auth';
import { customFetch } from './customFetch';

export const oauthAPI = () => {
    const authURL = `https://accounts.google.com/o/oauth2/auth?client_id=${import.meta.env.VITE_GOOGLE_CLIENT_ID}&redirect_uri=${import.meta.env.VITE_GOOGLE_REDIRECT_URL}&response_type=code&scope=openid%20email%20profile&access_type=offline&prompt=consent`;

    window.open(
        authURL,
        'Google Login',
        'width=500, height=600, left=100, top=100',
    );
};

export const getTokenAPI = async (
    accessToken: string,
): Promise<OAuthResponse> => {
    try {
        const response = await fetch(
            `${import.meta.env.VITE_GOOGLE_OAUTH_BACKEND_ENDPOINT}?code=${accessToken}`,
            {
                method: 'GET',
                credentials: 'include',
            },
        );

        const data = await response.json();
        return data;
    } catch (error) {
        console.log('error: ', error);

        throw error;
    }
};

export const getUserInfoAPI = async (
    accessToken: string | null,
): Promise<UserResponse> => {
    if (accessToken === null) {
        throw new Error('AccessToken이 없습니다.');
    }

    try {
        const response = await customFetch('/user', {
            method: 'GET',
            credentials: 'include',
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
        });

        const data = await response.json();

        const filteredData = {
            name: data.name,
            nickname: data.nickname,
            profileUri: data.profileUri,
        };

        return filteredData;
    } catch (error) {
        console.log('getUserInfo 에러: ', error);

        throw error;
    }
};
