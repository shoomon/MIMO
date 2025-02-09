import CategoryList from '@/components/molecules/CategoryList/CategoryList';
import ListContainer from '@/components/organisms/ListContainer';
import { useLocation } from 'react-router-dom';
import { CardMeeting } from '@/components/molecules';
import { TeamAPI } from '@/apis';
import { CardMeetingProps } from '@/components/molecules/CardMeeting/CardMeeting';
import { SimpleTeamResponse, TeamInfosResponse } from './../types/Team';
import { Title } from '@/components/atoms';
import { SimpleTeamResponseMok } from './../mock/mock';
import { CardMeetingmockData } from '@/mock/mock';

const Category = () => {
    const location = useLocation();
    const category = location.pathname.split('/').pop() || '';
    //   const { data, isLoading, error } = TeamAPI.getTeamInfosByCategory(category);

    // API 호출 대신 목업 데이터 사용

    const { data, isLoading, error } = {
        data: CardMeetingmockData,
        isLoading: false,
        error: null,
    };

    const size = CardMeetingmockData.teams.length;

    const CategoryMeetingList = data?.teams.map(
        (item: SimpleTeamResponseMok) => {
            const formattedTags = item.tags.map((tag) => ({
                label: tag,
                to: `search/tags`,
            }));

            return (
                <CardMeeting
                    key={item.teamId}
                    label={item.name}
                    content={item.description}
                    rating={item.rating}
                    tagList={formattedTags}
                    reviewCount={item.reviewCount}
                    image={{
                        memberCount: item.memberCount,
                        memberLimit: item.memberLimit,
                        imgSrc: item.thumbnail,
                        showMember: true,
                    }}
                    to={`/meeting/${item.teamId}`}
                    // 필요한 경우 추가 props 매핑
                />
            );
        },
    );

    return (
        <>
            <>
                <CategoryList />
                <div className="py-8">
                    <div className="mb-4 text-xl font-bold">
                        {category} 관련 모임 {size}개가 검색되었습니다.
                    </div>
                    <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
                        {CategoryMeetingList ?? <div>모임이 없습니다.</div>}
                    </div>
                </div>
            </>
        </>
    );
};

export default Category;
