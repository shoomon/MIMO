// src/pages/Search/TitleSearch.tsx
import { useSearchParams } from 'react-router-dom';
import BaseLayout from '../layouts/BaseLayout';
import { useQuery } from '@tanstack/react-query';
import { CardMeeting, Pagenation } from '@/components/molecules';
import { TeamtagSearch } from '@/apis/SearchAPI';
import { SimpleTeamResponse } from '@/types/Team';
import tagFormatter from '@/utils/tagFormatter';

export default function TitleSearch() {
    const [searchParams, setSearchParams] = useSearchParams();

    // 쿼리 파라미터: 검색어, 현재 페이지
    const searchKeyword = searchParams.get('searchKeyword') || '';
    const currentPage = parseInt(searchParams.get('pageNumber') || '1', 10);

    const {
        data: searchData,
        isLoading,
        isError,
    } = useQuery({
        queryKey: ['tagSearch', searchKeyword, currentPage],
        queryFn: () => TeamtagSearch(searchKeyword, String(currentPage)),
        enabled: !!searchKeyword,
    });

    // 한 페이지당 10개씩 → 전체 페이지 수 계산
    // searchData.numOfTeams가 전체 게시글 수
    const totalItems = searchData?.numberOfTeams ?? 0;
    const totalPages = Math.ceil(totalItems / 10);

    // 왼쪽 화살표 클릭 (이전 페이지)
    const handleClickLeft = () => {
        if (currentPage > 1) {
            setSearchParams({
                searchKeyword,
                pageNumber: String(currentPage - 1),
            });
        }
    };

    // 오른쪽 화살표 클릭 (다음 페이지)
    const handleClickRight = () => {
        if (currentPage < totalPages) {
            setSearchParams({
                searchKeyword,
                pageNumber: String(currentPage + 1),
            });
        }
    };

    // 페이지 번호 직접 클릭
    const handleClickPage = (e: React.MouseEvent<HTMLButtonElement>) => {
        const selectedPage = Number(e.currentTarget.textContent);
        if (selectedPage >= 1 && selectedPage <= totalPages) {
            setSearchParams({
                searchKeyword,
                pageNumber: String(selectedPage),
            });
        }
    };

    // ===================== 렌더링 분기 =====================

    // 1) 검색어가 없는 경우
    if (!searchKeyword) {
        return (
            <BaseLayout>
                <div className="flex flex-1 items-center justify-center p-4 text-center">
                    검색어를 입력해 주세요
                </div>
            </BaseLayout>
        );
    }

    // 2) 로딩/에러 처리
    if (isLoading) {
        return (
            <BaseLayout>
                <div className="flex flex-1 items-center justify-center p-4 text-center">
                    로딩 중...
                </div>
            </BaseLayout>
        );
    }

    if (isError) {
        return (
            <BaseLayout>
                <div className="flex flex-1 items-center justify-center p-4 text-center">
                    에러가 발생했습니다.
                </div>
            </BaseLayout>
        );
    }

    // 3) 검색 결과가 없는 경우
    if (!searchData || totalItems === 0) {
        return (
            <BaseLayout>
                <div className="flex flex-1 flex-col items-center justify-center p-4 text-center">
                    <h1 className="text-3xl font-bold">'{searchKeyword}'</h1>
                    <span className="text-3xl font-light">
                        검색 결과가 없습니다.
                    </span>
                </div>
            </BaseLayout>
        );
    }

    // 4) 검색 결과 렌더링
    // 현재 페이지에 해당하는 teams
    const searchTeam = searchData.teams?.map((item: SimpleTeamResponse) => {
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
    });

    return (
        <BaseLayout>
            {/* 검색 결과 헤더 */}
            <div className="flex flex-col items-center justify-center gap-1 p-4 text-center">
                <div className="flex flex-wrap items-center justify-center gap-2">
                    <h1 className="text-3xl font-bold">'{searchKeyword}'</h1>
                    <span className="text-3xl font-light">검색 결과</span>
                    <span className="text-3xl font-bold">{totalItems}</span>
                    <span className="text-3xl font-light">
                        건이 검색 되었습니다.
                    </span>
                </div>
            </div>

            {/* 그리드 형태로 카드 표시 */}
            <div className="grid grid-cols-1 gap-6 p-4 sm:grid-cols-2 lg:grid-cols-3">
                {searchTeam}
            </div>

            {/* 페이지네이션 영역 */}
            <div className="my-8 flex items-center justify-center">
                <Pagenation
                    currentPage={currentPage}
                    pageSize={totalPages} // 전체 페이지 수
                    onClickLeft={handleClickLeft}
                    onClickRight={handleClickRight}
                    onClick={handleClickPage}
                />
            </div>
        </BaseLayout>
    );
}
