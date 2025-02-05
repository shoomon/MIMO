interface PngIconViewProps {
    id: string;
    size?: number;
}

const PngIconView = ({ id, size = 72, ...props }: PngIconViewProps) => {
    return (
        <div
            className={`sprite bg-${id}`}
            style={{ width: `${size}px`, height: `${size}px` }}
            {...props}
            role="img"
            aria-label={`${id} icon`}
        ></div>
    );
};

export default PngIconView;
