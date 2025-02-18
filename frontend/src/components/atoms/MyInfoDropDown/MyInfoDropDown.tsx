import { ProfileImageProps } from '../ProfileImage/ProfileImage';
import MyInfoDropDownView from './MyInfoDropDown.view';
import { useTokenStore } from './../../../stores/tokenStore';
import { customFetch } from '@/apis/customFetch';
import { useQueryClient } from '@tanstack/react-query';

interface MyInfoDropDownProps {
    userInfo?: ProfileImageProps;
    active: boolean;
    addStyle: string;
    setActive: React.Dispatch<React.SetStateAction<boolean>>;
    setLogin: React.Dispatch<React.SetStateAction<boolean>>;
}

const logoutapi = async (): Promise<void> => {
    try {
        await customFetch('/login/oauth2/logout', {
            method: 'POST',
            credentials: 'include',
        });

        console.log('로그아웃됨');
    } catch (error) {
        console.error('Error fetching area teams:', error);
        throw error;
    }
};

const MyInfoDropDown = ({
    userInfo,
    active,
    addStyle,
    setActive,
    setLogin,
}: MyInfoDropDownProps) => {
    const { logout } = useTokenStore();
    const queryClient = useQueryClient();

    if (!userInfo) {
        return;
    }

    const handleLogout = () => {
        logout();
        logoutapi();
        queryClient.removeQueries({ queryKey: ['userInfo'] });
        setActive(false);
        setLogin(false);
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
