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
        { iconId: 'Bike', content: '바이크', path: 'category/bike' },
        { iconId: 'Book', content: '독서', path: 'category/book' },
        { iconId: 'Car', content: '자동차', path: 'category/car' },
        { iconId: 'CarWash', content: '세차', path: 'category/carwash' },
        { iconId: 'Cooking', content: '요리', path: 'category/cooking' },
        { iconId: 'Dog', content: '반려동물', path: 'category/dog' },
        { iconId: 'Exercise', content: '스포츠', path: 'category/exercise' },
        { iconId: 'Game', content: '게임', path: 'category/game' },
        { iconId: 'Gym', content: '헬스', path: 'category/gym' },
        { iconId: 'Music', content: '음악 / 악기', path: 'category/music' },
        { iconId: 'Camera', content: '사진 / 영상상', path: 'category/camera' },
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
