import { DETAIL_NAV_ITEMS } from '@/constants/navItems';
import DetailNavView from './DetailNav.view';

const DetailNav = () => {
    const navData = { navItems: DETAIL_NAV_ITEMS };

    return <DetailNavView {...navData} />;
};

export default DetailNav;
