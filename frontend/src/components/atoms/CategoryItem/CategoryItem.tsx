import Icon from '../Icon/Icon';

interface CategoryItemProps {
    iconId: string;
    content: string;
    onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

/**
 * `CategoryItem` 컴포넌트는 아이콘과 텍스트를 포함한 버튼을 렌더링합니다.
 *
 * @component
 * @param {string} iconId - 아이콘의 ID입니다.
 * @param {string} content - 버튼의 텍스트 내용입니다.
 * @param {(e: React.MouseEvent<HTMLButtonElement>) => void} [onClick] - 클릭 이벤트 핸들러입니다.
 * @returns {JSX.Element} - 렌더링된 `CategoryItem` 컴포넌트를 반환합니다.
 */

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
