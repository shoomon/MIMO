import { Link } from 'react-router-dom';

export interface TitleProps {
    label: string;
    to?: string;
    onClick?: (e: React.MouseEvent<HTMLAnchorElement>) => void;
}

/**
 * Title 컴포넌트의 Props.
 * @typedef {Object} TitleProps
 * @property {string} label - 제목으로 표시할 텍스트.
 * @property {string} [to] - 클릭 시 이동할 선택적 URL.
 * @property {(e: React.MouseEvent<HTMLAnchorElement>) => void} [onClick] - 링크 클릭 이벤트 핸들러 (선택적).
 */

/**
 * 텍스트를 표시하는 제목 컴포넌트입니다. `to` 속성이 제공되면 링크로 렌더링됩니다.
 * @param {TitleProps} props - 컴포넌트의 속성.
 * @returns {JSX.Element} 렌더링된 제목 컴포넌트.
 */

const Title = ({ label, to, onClick }: TitleProps) => {
    return to ? (
        <Link
            to={to}
            onClick={onClick}
            className="text-display-xs flex h-fit w-fit gap-1 font-bold text-black"
        >
            <span>{label}</span>
            <span className="font-normal">{'>'}</span>
        </Link>
    ) : (
        <div className="text-display-xs flex h-fit w-fit gap-1 font-bold text-black">
            {label}
        </div>
    );
};

export default Title;
