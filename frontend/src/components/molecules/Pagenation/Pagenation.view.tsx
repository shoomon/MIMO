import { Icon } from '@/components/atoms';

interface PagenationViewProps {
    currentPage: number;
    currentPageList: number[];
    onClickLeft: (e: React.MouseEvent<HTMLButtonElement>) => void;
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
    onClickRight: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const PagenationView = ({
    currentPage,
    currentPageList,
    onClickLeft,
    onClickRight,
    onClick,
}: PagenationViewProps) => {
    return (
        <section className="flex gap-4">
            <button
                type="button"
                onClick={onClickLeft}
                className={`flex h-8 w-8 cursor-pointer items-center justify-center rounded-xs bg-gray-100`}
            >
                <Icon id="Arrow-Left" type="svg" />
            </button>
            {currentPageList.map((item) => {
                return (
                    <button
                        type="button"
                        onClick={onClick}
                        className={`${item === currentPage ? 'text-brand-primary-400' : 'hover:text-brand-primary-400 text-gray-500'} h-8 w-8 cursor-pointer rounded-xs bg-gray-100 text-sm`}
                    >
                        {item}
                    </button>
                );
            })}
            <button
                type="button"
                onClick={onClickRight}
                className={`flex h-8 w-8 cursor-pointer items-center justify-center rounded-xs bg-gray-100`}
            >
                <Icon id="Arrow-Right" type="svg" />
            </button>
        </section>
    );
};

export default PagenationView;
