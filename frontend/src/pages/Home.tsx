import { CardMeeting } from '@/components/molecules';
import { CardMeetingProps } from '@/components/molecules/CardMeeting/CardMeeting';
import CategoryList from '@/components/molecules/CategoryList/CategoryList';
import ListContainer from '@/components/organisms/ListContainer';

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
        </>
    );

    //등등등...
};

export default Home;
