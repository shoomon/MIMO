import { CardMeeting } from '@/components/molecules';
import { CardMeetingProps } from '@/components/molecules/CardMeeting/CardMeeting';
import CategoryList from '@/components/molecules/CategoryList/CategoryList';
import ListContainer from '@/components/organisms/ListContainer';
import { SimpleTeamResponse } from './../types/Team';
import { CardMeetingmockData } from '@/mock/mock';
import { SimpleTeamResponseMok } from './../mock/mock';

// //여기서 type로 getMyMeeting Type을 만들고 CardMeetingProp랑 바꾸면 될듯?
// const MyMeetingList = MyMeetingListapi.map((item: CardMeetingProps) => {
//     const { meetingId, ...rest } = item;
//     const processedTo = `/meeting/${meetingId}`;
//     return <CardMeeting {...rest} to={processedTo} />;
// });

// const RecentMeetingList = RecentMeetingListapi.map((item: CardMeetingProps) => {
//     const { meetingId, ...rest } = item;
//     const processedTo = `/meeting/${meetingId}`;
//     return <CardMeeting {...rest} to={processedTo} />;
// });

// const { data, isLoading, error } = TeamAPI.getTeamInfosByArea(Area.GYEONGGI);

const { data, isLoading, error } = {
    data: CardMeetingmockData,
    isLoading: false,
    error: null,
};

const MeetingListByArea = data?.teams.map((item: SimpleTeamResponseMok) => {
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
        />
    );
});

const Home = () => {
    return (
        <>
            <CategoryList />
            {/* <div className="py-8">
                <ListContainer
                    to="./mymeetinglist"
                    gap="4"
                    label="나의 모임"
                    items={MyMeetingList}
                />
                ;
            </div>
            <div className="py-8">
                <ListContainer
                    to="./recentmeetinglist"
                    gap="4"
                    label="최근 활동한 모임"
                    items={RecentMeetingList}
                />
                ;
            </div> */}
            <div className="py-8">
                <ListContainer
                    to="./recentmeetinglist"
                    gap="4"
                    label="내 지역의 모임"
                    items={MeetingListByArea}
                />
                ;
            </div>
        </>
    );

    //등등등...
};

export default Home;
