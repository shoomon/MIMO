import { Link } from 'react-router-dom';
import Icon from '../Icon/Icon';

export interface CategoryItemProps {
    iconId: string;
    content: string;
    path: string;
    isSelected?: boolean;
    onClick?: (path: string, event: React.MouseEvent) => void;
}

/**
 * `CategoryItem` 컴포넌트는 아이콘과 텍스트를 포함한 버튼을 렌더링합니다.
 *
 * @component
 * @param {string} iconId - 아이콘의 ID입니다.
 * @param {string} content - 버튼의 텍스트 내용입니다.
 * @param {string} path - 이동할 경로입니다.
 * @returns {JSX.Element} - 렌더링된 `CategoryItem` 컴포넌트를 반환합니다.
 */

const CategoryItem = ({
    iconId,
    content,
    path,
    isSelected = false,
    onClick,
}: CategoryItemProps) => {
    const handleClick = (event: React.MouseEvent) => {
        if (onClick) {
            event.preventDefault();
            onClick(path, event);
        }
    };

    return (
        <Link
            to={path}
            onClick={handleClick}
            className={`flex h-fit w-[100px] flex-col items-center justify-center gap-3 rounded-lg py-2 ${isSelected ? 'bg-blue-100 hover:bg-blue-200' : 'bg-white hover:bg-gray-200'}`}
        >
            <div>
                <Icon type="png" id={iconId} />
            </div>
            <div
                className={`w-fit justify-center text-lg font-medium ${isSelected ? 'text-black' : 'text-black'}`}
            >
                {content}
            </div>
        </Link>
    );
};

export default CategoryItem;
