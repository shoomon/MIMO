import { useEffect } from 'react';
import HeaderView, { HeaderViewProps } from './Header.view';
import { logoutapi, reissueTokenAPI } from '@/apis/AuthAPI';
import { useTokenStore } from '@/stores/tokenStore';
import { useAuth } from '@/hooks/useAuth';

const Header = (props: HeaderViewProps) => {
    const { logout } = useTokenStore();
    const { setLogin } = useAuth();

    useEffect(() => {
        const validateToken = async () => {
            try {
                await reissueTokenAPI();
            } catch (error) {
                logout();
                await logoutapi();
                setLogin(false);
                console.log(error);
            }
        };

        validateToken();
    }, []);

    return <HeaderView {...props} />;
};

export default Header;
