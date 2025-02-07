import { Link } from 'react-router-dom';

export interface TitleProps {
    label: string;
    to?: string;
}

const Title = ({ label, to }: TitleProps) => {
    // to가 있으면 Link, 없으면 'div'를 Component 변수에 할당합니다.
    const Component = to ? Link : 'div';

    return (
        <Component
            // Link 컴포넌트에만 to 속성을 전달합니다.
            to={`${to}`}
            className="text-display-xs flex h-fit w-fit gap-1 font-bold text-black"
        >
            <span>{label}</span>
            {to && <span className="font-normal">{'>'}</span>}
        </Component>
    );
};

export default Title;
