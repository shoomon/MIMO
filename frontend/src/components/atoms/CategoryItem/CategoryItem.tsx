import Icon from '../Icon/Icon';

interface CategoryItemProps {
    iconId: string;
    content: string;
    onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const CategoryItem = ({ iconId, content, onClick }: CategoryItemProps) => {
    return (
        <button
            onClick={onClick}
            className="flex h-fit w-[100px] flex-col items-center justify-center gap-3 rounded-lg bg-white py-2 hover:bg-gray-100"
        >
            <div>
                <Icon type="png" id={iconId} />
            </div>
            <div className="w-fit justify-center text-lg font-medium text-black">
                {content}
            </div>
        </button>
    );
};

export default CategoryItem;
