import { CardMeeting } from '@/components/molecules';
import CategoryList from '@/components/molecules/CategoryList/CategoryList';
import ListContainer from '@/components/organisms/ListContainer';
import { Area, SimpleTeamResponse } from './../types/Team';
import { getTeamInfosByArea } from '@/apis/TeamAPI';
import { useQuery } from '@tanstack/react-query';

const Home = () => {
    const { data, isLoading, error } = useQuery({
        queryKey: ['Area', Area.GYEONGGI],
        queryFn: () => getTeamInfosByArea(Area.GYEONGGI),
    });

    // null 병합 연산자를 사용하여 기본값 빈 배열 제공
    const MeetingListByArea =
        data?.teams?.map((item: SimpleTeamResponse) => {
            const formattedTags = item.tags.map((tag) => ({
                label: tag,
                to: `search/${tag}`,
            }));

            return (
                <CardMeeting
                    key={item.teamId}
                    label={item.name}
                    content={item.description}
                    rating={item.reviewScore}
                    tagList={formattedTags}
                    reviewCount={item.reviewScore}
                    image={{
                        memberCount: item.memberCount,
                        memberLimit: item.maxCapacity,
                        imgSrc: item.teamProfileUri,
                        showMember: true,
                    }}
                    to={`/team/${item.teamId}`}
                />
            );
        }) ?? [];

    if (isLoading) return <div>Loading...</div>;
    if (error) return <div>Error loading teams</div>;

    return (
        <>
            <CategoryList />
            <div className="py-8">
                <ListContainer
                    to="./recentmeetinglist"
                    gap="4"
                    label="내 지역의 모임"
                    items={MeetingListByArea}
                />
            </div>
        </>
    );
};

export default Home;
