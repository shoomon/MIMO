import CategoryItem, {
    CategoryItemProps,
} from '@/components/atoms/CategoryItem/CategoryItem';
import ListContainer from '@/components/organisms/ListContainer';

interface CategoryListViewProps {
    to?: string;
    label: string;
    items: CategoryItemProps[];
}

const CategoryListView = ({ items, to }: CategoryListViewProps) => {
    const ItemList = items.map((item) => {
        return <CategoryItem {...item} />;
    });

    return <ListContainer label="카테고리" to={to} gap="4" items={ItemList} />;
};

export default CategoryListView;
