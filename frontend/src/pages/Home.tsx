import { CardMeeting } from '@/components/molecules';
import CategoryList from '@/components/molecules/CategoryList/CategoryList';
import ListContainer from '@/components/organisms/Carousel/ListContainer';
import { SimpleTeamResponse } from './../types/Team';
import { getMyTeamInfos, getTeamInfosByArea, getArea } from '@/apis/TeamAPI';
import { useQuery } from '@tanstack/react-query';
import tagFormatter from '@/utils/tagFormatter';
import { useAuth } from '@/hooks/useAuth';
import useLocation from '@/hooks/useLocation'; // 커스텀 훅, 앞서 정의한 useLocation

const Home = () => {
    const { userInfo } = useAuth();

    // 현재 위치 정보를 가져옴 (예: { region: '서울', address: '...' })
    const { locationData, loading: locationLoading } = useLocation();

    // areaList API 호출 (TagsResponse: { tags: string[] })
    const { data: areaData } = useQuery({
        queryKey: ['areaList'],
        queryFn: () => getArea(),
    });

    // fallback 지역: areaData가 있으면 tags의 첫 번째 값, 없으면 기본 '서울'
    const fallbackRegion =
        areaData && areaData.tags && areaData.tags.length > 0
            ? areaData.tags[0]
            : '서울';

    // 위치 정보를 허용하지 않거나 에러가 발생하면 locationData는 null이므로 fallback 사용
    const region =
        locationData && areaData && areaData.tags.includes(locationData.region)
            ? locationData.region
            : fallbackRegion;

    // 위치 정보 수락 여부에 따라 라벨을 다르게 표시
    const areaLabel = locationData ? '내 주변의 모임' : `${region}의 모임`;

    // teamInfosByArea 쿼리 실행: areaData가 준비되어 있으면 실행 (locationData는 없어도 fallback 사용)
    const { data, isLoading, error } = useQuery({
        queryKey: ['Area', region],
        queryFn: () => getTeamInfosByArea(region),
        enabled: !!areaData, // areaData가 준비되면 실행
    });

    const { data: MyTeamList } = useQuery({
        queryKey: ['MyTeamList', userInfo?.nickname],
        queryFn: () => getMyTeamInfos(),
        enabled: userInfo !== undefined,
    });

    // 팀 정보를 CardMeeting 컴포넌트로 변환
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

    // 로딩 상태: 팀 데이터와 areaData 로딩만 처리
    if (isLoading || locationLoading) return <div>Loading...</div>;
    if (error) return <div>Error loading teams</div>;

    return (
        <>
            <CategoryList />
            {userInfo != undefined && (
                <div className="py-8">
                    <ListContainer
                        to="./mymeeting"
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
                    label={areaLabel}
                    items={MeetingListByArea}
                    content="모임이 없습니다."
                />
            </div>
        </>
    );
};

export default Home;
