import MainNavItemView from './MainNavItem.view';

export interface MainNavItemProps {
    value: string;
    path: string;
}

const MainNavItem = (props: MainNavItemProps) => {
    return <MainNavItemView {...props} />;
};

export default MainNavItem;
