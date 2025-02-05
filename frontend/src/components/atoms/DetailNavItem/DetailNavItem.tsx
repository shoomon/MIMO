import DetailNavItemView from './DetailNavItem.view';

export interface DetailNavItemProps {
    item: string;
    icon: string;
    link: string;
}

const DetailNavItem = ({ item, icon, link }: DetailNavItemProps) => {
    return <DetailNavItemView item={item} icon={icon} link={link} />;
};

export default DetailNavItem;
