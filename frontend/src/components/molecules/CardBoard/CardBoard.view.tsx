// CardBoardView.tsx
import { Link } from 'react-router-dom';
import { ThumbnailProps } from '../Thumbnail/Thumbnail.view';
import Thumbnail from '../Thumbnail/Thumbnail';

export interface CardBoardViewProps {
    image: ThumbnailProps;
    label: string;
    author: string;
    parsedDate: string;
    viewCount: number;
    layoutType: 'List' | 'Card';
    linkTo: string;
}

const CardBoardView: React.FC<CardBoardViewProps> = ({
    image,
    label,
    author,
    parsedDate,
    viewCount,
    layoutType,
    linkTo,
}) => {
    return (
        <Link
            to={linkTo}
            className={`${
                layoutType === 'Card'
                    ? 'flex h-[275px] w-[344px] flex-col overflow-hidden rounded-t-2xl'
                    : 'flex items-center'
            } gap-3 bg-white`}
        >
            {layoutType === 'Card' ? (
                <Thumbnail
                    imgSrc={image.imgSrc}
                    imgAlt="게시글썸네일"
                    showMember={false}
                />
            ) : (
                <div className="flex h-[84px] w-[84px] flex-shrink-0 items-center justify-center overflow-hidden rounded-2xl">
                    <img
                        src={image.imgSrc}
                        alt="게시글썸네일"
                        className="h-full w-full object-cover"
                    />
                </div>
            )}

            <div className="flex h-fit w-full flex-col gap-1">
                <span className="truncate text-lg font-bold">{label}</span>
                <span className="text-md font-medium">{author}</span>
                <div className="text-md flex gap-4 font-medium">
                    <span>{parsedDate}</span>
                    <span>조회수 {viewCount}</span>
                </div>
            </div>
        </Link>
    );
};

export default CardBoardView;
