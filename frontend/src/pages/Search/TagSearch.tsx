// src/pages/Search/TitleSearch.tsx
import { useSearchParams } from 'react-router-dom';
import { CardMeeting } from '@/components/molecules';
import { useQuery } from '@tanstack/react-query';
import { TeamTitleSearch } from '@/apis/SearchAPI';
import { SimpleTeamResponse } from '@/types/Team';
import tagFormatter from '@/utils/tagFormatter';
// import { SimpleTeamResponse } from '@/types/Team'; // 실제 타입이 있다면 주석 해제

export default function TitleSearch() {
    const [searchParams] = useSearchParams();
    const searchKeyword = searchParams.get('searchKeyword') || '';
    const pageNumber = searchParams.get('pageNumber') || '1';

    // React Query 사용 (캐싱, 로딩/에러 관리 등)

    const {
        data: searchData,
        isLoading,
        isError,
    } = useQuery({
        queryKey: ['titleSearch', searchKeyword],
        queryFn: () => TeamTitleSearch(searchKeyword, pageNumber),
    });

    if (!searchKeyword) {
        // 쿼리 파라미터가 없을 때 → 검색어 입력 전 상태
        return <div>검색어를 입력해 주세요</div>;
    }

    if (isLoading) return <div>로딩 중...</div>;
    if (isError) return <div>에러가 발생했습니다.</div>;

    // 실제 응답 구조에 맞춰서 데이터 사용
    if (!searchData || searchData.size === 0) {
        return <div>검색 결과가 없습니다.</div>;
    }

    // teamData가 배열이라고 가정

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
        <div>
            <h1>제목·설명 검색 결과</h1>
            {searchTeam}
        </div>
    );
}
