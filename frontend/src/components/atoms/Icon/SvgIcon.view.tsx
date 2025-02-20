interface SvgIconViewProps extends React.SVGProps<SVGSVGElement> {
    id: string;
    size?: number;
}

const SvgIconView = ({ id, size = 20, ...props }: SvgIconViewProps) => {
    const spriteUrl = `/_sprite.svg?v=${Date.now()}`; // 캐시 무효화

    return (
        <svg
            width={size}
            height={size}
            viewBox="0 0 20 20"
            xmlns="http://www.w3.org/2000/svg"
            {...props}
        >
            <use href={`${spriteUrl}#${id}`} xlinkHref={`${spriteUrl}#${id}`} />
        </svg>
    );
};

export default SvgIconView;
