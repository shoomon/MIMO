import CategoryListView from './CategoryList.view';

const CategoryList = () => {
    // api로 해야함 나중에 수정 필요
    const categoryList = [
        { iconId: 'Bike', content: '바이크', path: '/' },
        { iconId: 'Book', content: '독서', path: '/' },
        { iconId: 'Car', content: '자동차', path: '/' },
        { iconId: 'CarWash', content: '세차', path: '/' },
        { iconId: 'Cooking', content: '요리', path: '/' },
        { iconId: 'Dog', content: '반려동물', path: '/' },
        { iconId: 'Exercise', content: '스포츠', path: '/' },
        { iconId: 'Game', content: '게임', path: '/' },
        { iconId: 'Gym', content: '헬스', path: '/' },
        { iconId: 'Music', content: '음악 / 악기', path: '/' },
        { iconId: 'Camera', content: '사진 / 영상상', path: '/' },
    ];

    return <CategoryListView items={categoryList} />;
};

export default CategoryList;
