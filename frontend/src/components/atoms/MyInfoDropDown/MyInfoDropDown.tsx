import { ProfileImageProps } from '../ProfileImage/ProfileImage';
import MyInfoDropDownView from './MyInfoDropDown.view';

interface MyInfoDropDownProps {
    userInfo?: ProfileImageProps;
    active: boolean;
    addStyle: string;
}

const NoDataView = () => {
    return <div className="text-text text-sm">유저 정보가 없습니다.</div>;
};

const MyInfoDropDown = ({
    userInfo,
    active,
    addStyle,
}: MyInfoDropDownProps) => {
    if (!userInfo) {
        return <NoDataView />;
    }

    // 로그아웃 API 연결 필요
    const handleLogout = () => {
        alert('로그아웃!');
    };

    return (
        <MyInfoDropDownView
            userInfo={userInfo}
            active={active}
            addStyle={addStyle}
            onClick={handleLogout}
        />
    );
};

export default MyInfoDropDown;
