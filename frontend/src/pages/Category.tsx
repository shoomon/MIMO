import CategoryList from '@/components/molecules/CategoryList/CategoryList';
import { useParams } from 'react-router-dom';
import { CardMeeting } from '@/components/molecules';
import { SimpleTeamResponse } from './../types/Team';
import { getTeamInfosByCategory } from '@/apis/TeamAPI';
import { useQuery } from '@tanstack/react-query';
import { TeamInfosResponse } from '@/types/Team';

const Category = () => {
    const { categoryId } = useParams<{ categoryId?: string }>();

    //isLoading, error
    const { data } = useQuery<TeamInfosResponse, Error>({
        queryKey: ['category', categoryId],
        queryFn: () => getTeamInfosByCategory(categoryId!),
        enabled: Boolean(categoryId),
    });

    if (!categoryId) {
        return <div>유효한 카테고리 정보가 없습니다.</div>;
    }

    const size = data?.teams.length;

    // API 응답의 teams 배열을 CardMeeting 컴포넌트 목록으로 변환합니다.
    const meetingList =
        data?.teams.map((item: SimpleTeamResponse) => {
            const formattedTags = item.tags.map((tag) => ({
                label: tag,
                to: `/search/${tag}`, // 실제 링크가 있다면 동적 처리가 필요할 수 있음
            }));

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
                    // 필요한 경우 추가 props 매핑
                />
            );
        }) ?? [];

    return (
        <>
            <CategoryList />
            <div className="py-8">
                <div className="mb-4 text-xl font-bold">
                    {categoryId} 관련 모임 {size}개가 검색되었습니다.
                </div>
                <div className="grid grid-cols-1 gap-2 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
                    {meetingList ?? <div>모임이 없습니다.</div>}
                </div>
            </div>
        </>
    );
};

export default Category;
