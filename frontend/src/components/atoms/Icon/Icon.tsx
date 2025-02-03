import PngIconView from './PngIcon.view';
import SvgIconView from './SvgIcon.view';

interface IconProps extends React.SVGProps<SVGSVGElement> {
    /** 보여줄 아이콘의 타입을 지정합니다. svg 또는 png 를 선택 */
    type: 'svg' | 'png';
    /** 표시할 아이콘의 고유 식별자 */
    id: string;
    /** 아이콘의 크기 */
    size?: number;
}

const Icon = ({ type, id, size, ...props }: IconProps) => {
    return type === 'svg' ? (
        <SvgIconView id={id} size={size} {...props} />
    ) : (
        <PngIconView id={id} size={size} {...props} />
    );
};

export default Icon;
