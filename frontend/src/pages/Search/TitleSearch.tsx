// src/pages/Search/TitleSearch.tsx
import { useSearchParams } from 'react-router-dom';
import { CardMeeting } from '@/components/molecules';
import { useQuery } from '@tanstack/react-query';
import { TeamTitleSearch } from '@/apis/SearchAPI';
import { SimpleTeamResponse } from '@/types/Team';
import tagFormatter from '@/utils/tagFormatter';
import BaseLayout from '../layouts/BaseLayout';

export default function TitleSearch() {
    const [searchParams, setSearchParams] = useSearchParams();
    const searchKeyword = searchParams.get('searchKeyword') || '';
    // pageNumber를 숫자로 파싱 (기본값 1)
    const pageNumber = parseInt(searchParams.get('pageNumber') || '1', 10);

    // React Query 사용
    const {
        data: searchData,
        isLoading,
        isError,
    } = useQuery({
        queryKey: ['titleSearch', searchKeyword, pageNumber],
        queryFn: () => TeamTitleSearch(searchKeyword, String(pageNumber)),
        // 검색어가 없으면 요청 X (필요시)
        enabled: !!searchKeyword,
    });

    // 페이지네이션 핸들러
    const handlePrevPage = () => {
        // 1페이지 이하로 내려갈 수 없도록 처리
        if (pageNumber > 1) {
            setSearchParams({
                searchKeyword,
                pageNumber: String(pageNumber - 1),
            });
        }
    };

    const handleNextPage = () => {
        setSearchParams({
            searchKeyword,
            pageNumber: String(pageNumber + 1),
        });
    };

    // 1) 검색어가 없을 때 → 검색 전 상태
    if (!searchKeyword) {
        return <div>검색어를 입력해 주세요</div>;
    }

    // 2) 로딩/에러 상태
    if (isLoading) return <div>로딩 중...</div>;
    if (isError) return <div>에러가 발생했습니다.</div>;

    // 3) 검색 결과가 없는 경우
    if (!searchData || searchData.size === 0) {
        return (
            <div className="flex flex-1 flex-col items-center justify-center p-4 text-center">
                <h1 className="text-3xl font-bold">'{searchKeyword}'</h1>
                <span className="text-3xl font-light">
                    검색 결과가 없습니다.
                </span>
            </div>
        );
    }

    // 4) 검색 결과가 있는 경우
    const searchTeam =
        searchData?.teams?.map((item: SimpleTeamResponse) => {
            const formattedTags = tagFormatter(item.tags);
            return (
                <CardMeeting
                    key={item.teamId}
                    label={item.name}
                    content={item.description}
                    rating={item.reviewScore}
                    tagList={formattedTags}
                    image={{
                        memberCount: item.currentCapacity,
                        memberLimit: item.maxCapacity,
                        imgSrc: item.teamProfileUri,
                        showMember: true,
                    }}
                    to={`/team/${item.teamId}`}
                />
            );
        }) ?? [];

    return (
        <BaseLayout>
            {/* 헤더 영역 */}
            <div className="flex flex-col items-center justify-center gap-1 p-4 text-center">
                <div className="flex flex-wrap items-center justify-center gap-2">
                    <h1 className="text-3xl font-bold">'{searchKeyword}'</h1>
                    <span className="text-3xl font-light">검색 결과</span>
                    <span className="text-3xl font-bold">
                        {searchData.size}
                    </span>
                    <span className="text-3xl font-light">
                        건이 검색 되었습니다.
                    </span>
                </div>
            </div>

            {/* 검색 결과 (그리드 레이아웃) */}
            <div className="grid grid-cols-1 gap-6 p-4 sm:grid-cols-2 lg:grid-cols-3">
                {searchTeam}
            </div>

            {/* 페이지네이션 버튼 */}
            <div className="mt-8 flex items-center justify-center gap-4">
                <button
                    className="rounded border px-4 py-2 disabled:opacity-50"
                    onClick={handlePrevPage}
                    disabled={pageNumber <= 1}
                >
                    이전 페이지
                </button>
                <span>현재 페이지: {pageNumber}</span>
                <button
                    className="rounded border px-4 py-2"
                    onClick={handleNextPage}
                >
                    다음 페이지
                </button>
            </div>
        </BaseLayout>
    );
}
