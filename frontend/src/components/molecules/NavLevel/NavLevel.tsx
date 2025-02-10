import { useBreadcrumbs } from '@/hooks/useNav';
import NavLevelView from './NavLevel.veiw';

const NavLevel = () => {
    const breadcrumbs = useBreadcrumbs();

    const filterBreadCrumb = breadcrumbs.map((item, index) => {
        if (index == breadcrumbs.length - 1) {
            return { label: item.label };
        }

        return item;
    });

    return <NavLevelView navItems={filterBreadCrumb} />;
};

export default NavLevel;
