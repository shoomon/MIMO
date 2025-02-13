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
        { iconId: 'Bike', content: '바이크', path: 'category/BIKE' },
        { iconId: 'Book', content: '독서', path: 'category/BOOK' },
        { iconId: 'Car', content: '자동차', path: 'category/CAR' },
        { iconId: 'CarWash', content: '세차', path: 'category/CAR' },
        { iconId: 'Cooking', content: '요리', path: 'category/COOK' },
        { iconId: 'Dog', content: '반려동물', path: 'category/PET' },
        { iconId: 'Exercise', content: '스포츠', path: 'category/SPORTS' },
        { iconId: 'Game', content: '게임', path: 'category/GAME' },
        { iconId: 'Gym', content: '헬스', path: 'category/HEALTH' },
        { iconId: 'Music', content: '음악 / 악기', path: 'category/MUSIC' },
        { iconId: 'Camera', content: '사진 / 영상', path: 'category/PHOTO' },
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
