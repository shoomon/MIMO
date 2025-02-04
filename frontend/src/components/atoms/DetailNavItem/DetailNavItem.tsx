import { useNavActive } from '@/hooks/useNav';
import DetailNavItemView from './DetailNavItem.view';

export interface DetailNavItemProps {
    item: string;
    icon: string;
    link: string;
}

const DetailNavItem = ({ item, icon, link }: DetailNavItemProps) => {
    const isActive = useNavActive(link);

    return (
        <DetailNavItemView
            item={item}
            icon={icon}
            link={link}
            active={isActive}
        />
    );
};

export default DetailNavItem;
