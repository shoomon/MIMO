import CategoryItem, {
    CategoryItemProps,
} from '@/components/atoms/CategoryItem/CategoryItem';
import ListContainer from '@/components/organisms/Carousel/ListContainer';

interface CategoryListViewProps {
    to?: string;
    label: string;
    items: CategoryItemProps[];
    onClick: (path: string, event: React.MouseEvent) => void;
}

const CategoryListView = ({ items, to, onClick }: CategoryListViewProps) => {
    const ItemList = items.map((item) => {
        return <CategoryItem {...item} onClick={onClick} />;
    });

    return <ListContainer label="카테고리" to={to} gap="4" items={ItemList} />;
};

export default CategoryListView;
