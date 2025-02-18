import CategoryListView from './CategoryList.view';
import { useNavigate, useLocation } from 'react-router-dom';

const CategoryList = ({ to }: { to?: string }) => {
    const navigate = useNavigate();
    const location = useLocation();
    const currentPath = location.pathname;

    const handleClick = (path: string, event: React.MouseEvent) => {
        event.preventDefault();
        navigate(`/${path}`, { replace: true });
    };

    const categoryList = [
        { iconId: 'Bike', content: '바이크', path: 'category/바이크' },
        { iconId: 'Book', content: '독서', path: 'category/독서' },
        { iconId: 'Car', content: '자동차', path: 'category/자동차' },
        { iconId: 'CarWash', content: '세차', path: 'category/자동차' },
        { iconId: 'Cooking', content: '요리', path: 'category/요리' },
        { iconId: 'Dog', content: '반려동물', path: 'category/반려동물' },
        { iconId: 'Exercise', content: '스포츠', path: 'category/스포츠' },
        { iconId: 'Game', content: '게임', path: 'category/게임' },
        { iconId: 'Gym', content: '헬스', path: 'category/헬스' },
        { iconId: 'Music', content: '음악 / 악기', path: 'category/음악' },
        { iconId: 'Camera', content: '사진 / 영상', path: 'category/사진' },
    ].map((item) => ({
        ...item,
        isSelected: `/${item.path}` === currentPath,
    }));

    return (
        <CategoryListView
            items={categoryList}
            label="카테고리"
            to={to}
            onClick={handleClick}
        />
    );
};

export default CategoryList;
