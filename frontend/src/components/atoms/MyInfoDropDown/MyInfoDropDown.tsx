import { ProfileImageProps } from '../ProfileImage/ProfileImage';
import MyInfoDropDownView from './MyInfoDropDown.view';
import { useTokenStore } from './../../../stores/tokenStore';
import { customFetch } from '@/apis/customFetch';

interface MyInfoDropDownProps {
    userInfo?: ProfileImageProps;
    active: boolean;
    addStyle: string;
}

const logoutapi = async (): Promise<void> => {
    try {
        await customFetch('/login/oauth2/logout', {
            method: 'POST',
            credentials: 'include',
        });
        alert('로그아웃');
        console.log('로그아웃됨');
    } catch (error) {
        console.error('Error fetching area teams:', error);
        throw error;
    }
};

const NoDataView = () => {
    return <div className="text-text text-sm">유저 정보가 없습니다.</div>;
};

const MyInfoDropDown = ({
    userInfo,
    active,
    addStyle,
}: MyInfoDropDownProps) => {
    const { logout } = useTokenStore();

    if (!userInfo) {
        return <NoDataView />;
    }

    const handleLogout = () => {
        logout();
        logoutapi();
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
