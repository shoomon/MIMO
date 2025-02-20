import { Link } from 'react-router-dom';

export interface TagProps {
    label: string;
}

/**
 * Tag 컴포넌트의 Props.
 * @typedef {Object} TagProps
 * @property {string} label - 태그에 표시할 텍스트.
 */

/**
 * 네비게이션 링크를 제공하는 태그 컴포넌트입니다.
 * @param {TagProps} props - 컴포넌트의 속성.
 * @returns {JSX.Element} 렌더링된 태그 컴포넌트.
 */
const Tag = ({ label }: TagProps) => {
    return (
        <Link
            to={`/search/tags?searchKeyword=${label}&pageNumber=1`}
            className="text-md border-brand-primary-400 text-brand-primary-400 h-fit w-fit rounded-sm border-[1px] bg-white px-[6px] py-[2px] font-semibold"
        >
            {label}
        </Link>
    );
};

export default Tag;
