import { CardMeeting } from '@/components/molecules';
import CategoryList from '@/components/molecules/CategoryList/CategoryList';
import ListContainer from '@/components/organisms/Carousel/ListContainer';
import { SimpleTeamResponse } from './../types/Team';
import { getMyTeamInfos, getTeamInfosByArea } from '@/apis/TeamAPI';
import { useQuery } from '@tanstack/react-query';
import tagFormatter from '@/utils/tagFormatter';
import { useAuth } from '@/hooks/useAuth';

const Home = () => {
    const { userInfo } = useAuth();

    const { data, isLoading, error } = useQuery({
        queryKey: ['Area', '서울'],
        queryFn: () => getTeamInfosByArea('서울'),
    });

    const { data: MyTeamList } = useQuery({
        queryKey: ['MyTeamList', userInfo?.nickname],
        queryFn: () => getMyTeamInfos(),
        enabled: userInfo !== undefined,
    });

    // null 병합 연산자
    // null 병합 연산자를 사용하여 기본값 빈 배열 제공
    const MeetingListByArea =
        data?.teams?.map((item: SimpleTeamResponse) => {
            const formattedTags = tagFormatter(item.tags);
            return (
                <CardMeeting
                    key={item.teamId}
                    label={item.name}
                    content={item.description}
                    reviewScore={item.reviewScore}
                    reviewCount={item.reviewCount}
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

    const myMeetingList =
        MyTeamList?.teams?.map((item: SimpleTeamResponse) => {
            const formattedTags = tagFormatter(item.tags);
            return (
                <CardMeeting
                    key={item.teamId}
                    label={item.name}
                    content={item.description}
                    reviewScore={item.reviewScore}
                    reviewCount={item.reviewCount}
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

    if (isLoading) return <div>Loading...</div>;
    if (error) return <div>Error loading teams</div>;

    return (
        <>
            <CategoryList />
            {userInfo != undefined && (
                <div className="py-8">
                    <ListContainer
                        to="./recentmeetinglist"
                        gap="4"
                        label="나의 모임"
                        items={myMeetingList}
                        content="모임이 없습니다."
                    />
                </div>
            )}
            <div className="py-8">
                <ListContainer
                    to="./recentmeetinglist"
                    gap="4"
                    label="내 지역의 모임"
                    items={MeetingListByArea}
                    content="모임이 없습니다."
                />
            </div>
        </>
    );
};

export default Home;
