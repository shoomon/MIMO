import { DetailNavItem } from '@/components/atoms';
import { DetailNavItemProps } from '@/components/atoms/DetailNavItem/DetailNavItem.tsx';

export interface DetailNavProps {
    navItems: DetailNavItemProps[];
}

const DetailNavView = ({ navItems }: DetailNavProps) => {
    return (
        <ul className="flex gap-6 px-4 py-2">
            {navItems.map((item) => {
                return (
                    <li key={item.item}>
                        <DetailNavItem {...item} />
                    </li>
                );
            })}
        </ul>
    );
};

export default DetailNavView;
