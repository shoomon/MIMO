import { Link } from 'react-router-dom';

interface MenuItemProps {
    label: string;
    to: string;
    unit?: string;
}

/**
 * MenuItem 컴포넌트의 Props.
 * @typedef {Object} MenuItemProps
 * @property {string} label - 메뉴 항목에 표시할 텍스트.
 * @property {string} to - 이동할 URL.
 * @property {string} [unit] - 선택적으로 표시할 단위 텍스트 ex)마일리지.
 */

/**
 * 네비게이션을 위한 메뉴 항목 컴포넌트입니다.
 * @param {MenuItemProps} props - 컴포넌트의 속성.
 * @returns {JSX.Element} 렌더링된 메뉴 항목 컴포넌트.
 */
const MenuItem = ({ label, to, unit }: MenuItemProps) => {
    return (
        <Link
            to={to}
            className="flex h-fit w-[240px] items-center justify-start gap-1 bg-white px-2 py-[6px] hover:bg-gray-200"
        >
            <span className="text-md font-bold">{label}</span>
            {unit && <span className="text-sm font-normal">{unit}</span>}
        </Link>
    );
};

export default MenuItem;
