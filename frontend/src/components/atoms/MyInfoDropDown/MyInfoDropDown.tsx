import MyInfoDropDownView, { MyInfoDropDownProps } from './MyInfoDropDown.view';

const NoDataView = () => {
    return <div>유저 정보가 없습니다.</div>;
};

const MyInfoDropDown = ({ userInfo }: MyInfoDropDownProps) => {
    // 로그아웃 API 연결 필요
    const handleLogout = () => {};

    if (!userInfo) {
        return <NoDataView />;
    }

    return <MyInfoDropDownView userInfo={userInfo} onClick={handleLogout} />;
};

export default MyInfoDropDown;
