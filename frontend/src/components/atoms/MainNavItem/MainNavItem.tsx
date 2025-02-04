import { useNavActive } from '@/hooks/useNav';
import MainNavItemView from './MainNavItem.view';

export interface MainNavItemProps {
    value: string;
    path: string;
}

const MainNavItem = (props: MainNavItemProps) => {
    const isActive = useNavActive(props.path);

    return <MainNavItemView {...props} active={isActive} />;
};

export default MainNavItem;
