import { Navigate, Route, Routes } from 'react-router-dom';
import {
    Board,
    BoardCreate,
    BoardDetail,
    BoardEdit,
    Category,
    ChatPage,
    DefaultLayout,
    GoogleCallback,
    Home,
    MyPage,
    Payment,
    Search,
    TeamAdditionMember,
    TeamAlbum,
    TeamCreate,
    TeamCurrentMember,
    TeamDetail,
    TeamEdit,
    TeamLayout,
    TeamList,
    TeamMember,
    TeamMileage,
    TeamMileageInfo,
    TeamMileageNonPay,
    TeamMileagePay,
    TeamSchedule,
    TeamScheduleCreate,
    TeamScheduleEdit,
    VideoChat,
} from '@/pages';
import TeamScheduleDetail from './pages/TeamSchedule/TeamScheduleDetail';
import BoardPosts from './pages/Board/BoardPosts';
import TeamSchedulesAdHoc from './pages/TeamSchedule/TeamSchedulesAdHoc';
import TeamSchedulesRegular from './pages/TeamSchedule/TeamSchedulesRegular';
import TeamSchedulesClosed from './pages/TeamSchedule/TeamSchedulesClosed';
import Profile from './pages/Profile';
import TitleSearch from './pages/Search/TitleSearch';
import TagSearch from './pages/Search/TagSearch';
import Review from './pages/Review';
import MyMeeting from './pages/Member/MyMeeting';

const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/auth/google/callback" element={<GoogleCallback />} />
            {/* 메인 */}
            <Route element={<DefaultLayout />}>
                {/* 메인 홈 */}
                <Route path="/" element={<Home />} />
                {/* 검색 */}
                <Route path="/search" element={<Search />} />
                {/* 제목·설명 기반 검색 */}
                <Route path="/search/title" element={<TitleSearch />} />
                {/* 태그 기반 검색 */}
                <Route path="/search/tags" element={<TagSearch />} />
                {/* 카테고리 --- 이것만 카테고리 리스트 뜸 */}
                <Route path="/category/:categoryId" element={<Category />} />
                {/* 인기 모임 --- queryparam으로 */}
                <Route path="/list" element={<TeamList />} />
                <Route path="/mymeeting" element={<MyMeeting />} />
                {/* 마이 페이지 */}
                <Route path="/mypage" element={<MyPage />} />
                {/* 채팅  */}
                <Route path="/chat" element={<ChatPage />} />
                {/* 유저 프로필 프로필에 Id가 없으면 마이페이지로 간다잇  */}
                <Route
                    path="/profile"
                    element={<Navigate to="/mypage" replace />}
                />
                <Route path="/profile/:userId" element={<Profile />} />

                {/* 팀  */}
                <Route path="/team">
                    <Route index element={<Navigate to="/" replace />} />
                    {/* 팀 생성 */}
                    <Route path="create" element={<TeamCreate />} />
                    <Route path=":teamId" element={<TeamLayout />}>
                        {/* 팀 메인 */}
                        <Route index element={<TeamDetail />} />
                        {/* 팀 수정 */}
                        <Route path="edit" element={<TeamEdit />} />
                        <Route path="review" element={<Review />} />
                        {/* 팀 일정 */}
                        <Route path="schedule">
                            {/* 일정 메인 */}
                            <Route index element={<TeamSchedule />} />
                            {/* 일정 작성 */}
                            <Route
                                path="create"
                                element={<TeamScheduleCreate />}
                            />
                            {/* 일정 수정 */}
                            <Route
                                path="edit/:scheduleId"
                                element={<TeamScheduleEdit />}
                            />
                            {/* 기존 동적 라우트 (필요시 유지) */}
                            <Route
                                path=":scheduleId"
                                element={<TeamScheduleDetail />}
                            />

                            {/* 새로 추가된 경로들 */}
                            <Route
                                path="ad-hoc"
                                element={<TeamSchedulesAdHoc />}
                            />
                            <Route
                                path="regular"
                                element={<TeamSchedulesRegular />}
                            />
                            <Route
                                path="closed"
                                element={<TeamSchedulesClosed />}
                            />
                        </Route>
                        {/* 게시판 관련 라우트 */}
                        <Route path="board">
                            {/* 게시판 리스트 : 팀 내 전체 게시판 */}
                            <Route index element={<Board />} />
                            {/* 특정 게시판 선택 시 */}
                            <Route path=":teamBoardId">
                                {/* 해당 게시판의 게시글 목록 */}
                                <Route index element={<BoardPosts />} />
                                {/* 게시글 생성 */}
                                <Route
                                    path="create"
                                    element={<BoardCreate />}
                                />
                                {/* 게시글 상세 */}
                                <Route
                                    path="post/:postId"
                                    element={<BoardDetail />}
                                />
                                {/* 게시글 수정 */}
                                <Route
                                    path="edit/:postId"
                                    element={<BoardEdit />}
                                />
                            </Route>
                        </Route>
                        {/* 멤버 */}
                        <Route path="members">
                            {/* 멤버 메인 */}
                            <Route index element={<TeamMember />} />
                            {/* 현재 멤버만 */}
                            <Route
                                path="current"
                                element={<TeamCurrentMember />}
                            />
                            {/* 가입 신청 멤버만 */}
                            <Route
                                path="addition"
                                element={<TeamAdditionMember />}
                            />
                        </Route>
                        {/* 팀 마일리지 */}
                        <Route path="mileage">
                            {/* 마일리지 메인 */}
                            <Route index element={<TeamMileage />} />
                            {/* 사용내역 상세 */}
                            <Route
                                path="payInfo"
                                element={<TeamMileageInfo />}
                            />
                            {/* 납부내역 상세 */}
                            <Route
                                path="payment"
                                element={<TeamMileagePay />}
                            />
                            {/* 미납부내역 상세 */}
                            <Route
                                path="non-payment"
                                element={<TeamMileageNonPay />}
                            />
                        </Route>
                        {/* 팀 앨범 */}
                        <Route path="album">
                            <Route index element={<TeamAlbum />} />
                        </Route>
                    </Route>
                </Route>
            </Route>

            {/* 화상 채팅 */}
            <Route path="/video" element={<VideoChat />} />

            {/* QR 코드 */}
            <Route
                path="/pay/:uuid/:amount/:receiverAccountNumber/:memo"
                element={<Payment />}
            />
        </Routes>
    );
};
export default AppRoutes;
