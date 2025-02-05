import PagenationView from './Pagenation.view';
import { usePagenationList } from '@/hooks/usePagenation';

interface PagenationProps {
    currentPage: number;
    pageSize: number;
    onClickLeft: (e: React.MouseEvent<HTMLButtonElement>) => void;
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
    onClickRight: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const Pagenation = ({
    currentPage,
    pageSize,
    onClick,
    onClickLeft,
    onClickRight,
}: PagenationProps) => {
    const { currentPageList } = usePagenationList(currentPage, pageSize);

    return (
        <PagenationView
            currentPage={currentPage}
            currentPageList={currentPageList}
            onClick={onClick}
            onClickLeft={onClickLeft}
            onClickRight={onClickRight}
        />
    );
};

export default Pagenation;
