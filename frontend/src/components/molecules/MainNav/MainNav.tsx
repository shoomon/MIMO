import { MAIN_NAV_ITEMS } from '@/constants/navItems';
import MainNavView from './MainNav.view';

const MainNav = () => {
    const navData = { navItems: MAIN_NAV_ITEMS };

    return <MainNavView {...navData} />;
};

export default MainNav;
