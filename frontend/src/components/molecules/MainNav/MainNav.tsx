import { MAIN_NAV_ITEMS } from '@/constants/navItems';
import MainNavView from './MainNav.view';
import { useNavigate } from 'react-router-dom';

const MainNav = () => {
    const navData = { navItems: MAIN_NAV_ITEMS };
    const navigate = useNavigate();

    const createTeam = () => {
        navigate('/team/create');
    };
    return <MainNavView createTeam={createTeam} {...navData} />;
};

export default MainNav;
