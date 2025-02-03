interface SvgIconViewProps extends React.SVGProps<SVGSVGElement> {
    id: string;
    size?: number;
}

const SvgIconView = ({ id, size = 20, ...props }: SvgIconViewProps) => {
    return (
        <svg width={`${size}px`} height={`${size}px`} {...props}>
            <use href={`/_sprite.svg#${id}`} />
        </svg>
    );
};

export default SvgIconView;
