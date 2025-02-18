interface SvgIconViewProps extends React.SVGProps<SVGSVGElement> {
    id: string;
    size?: number;
}

const SvgIconView = ({ id, size = 20, ...props }: SvgIconViewProps) => {
    return (
        <svg width={`${size}px`} height={`${size}px`} {...props}>
            <use
                href={`${import.meta.env.BASE_URL}_sprite.svg#${id}`}
                width="100%"
                height="100%"
            />
        </svg>
    );
};

export default SvgIconView;
