export interface RelatedSearchItemViewProps {
    value: string;
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const RelatedSearchItemView = ({
    value,
    onClick,
}: RelatedSearchItemViewProps) => {
    return (
        <button
            role="option"
            onClick={onClick}
            className="text-text w-[37.5rem] px-3 py-2 text-left hover:bg-gray-200"
            aria-label={`${value} 검색하기`}
            data-value={value}
        >
            {value}
        </button>
    );
};

export default RelatedSearchItemView;
