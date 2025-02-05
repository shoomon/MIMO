import { Link } from 'react-router-dom';
import { ThumbnailProps } from '../Thumbnail/Thumbnail.view';
import { ProfileImage } from '@/components/atoms';
import Thumbnail from '../Thumbnail/Thumbnail';
import { dateParsing } from '@/utils';

interface CardBoardProps {
    image: ThumbnailProps;
    label: string;
    author: string;
    date: string;
    viewCount: number;
    layoutType: 'List' | 'Card';
}

const CardBoard = ({
    image,
    label,
    author,
    date,
    viewCount,
    layoutType,
}: CardBoardProps) => {
    const parseDate = dateParsing(date);

    return (
        <Link
            to="/"
            className={`${layoutType === 'Card' ? 'flex h-[275px] w-[344px] flex-col overflow-hidden rounded-t-2xl' : 'flex'} gap-3 bg-white`}
        >
            {layoutType == 'Card' ? (
                <Thumbnail
                    imgSrc={image.imgSrc}
                    imgAlt="게시글썸네일"
                    showMember={false}
                />
            ) : (
                <div className="h-[80px] w-[80px] rounded-2xl">
                    <img
                        src={image.imgSrc}
                        alt="게시글썸네일"
                        width="80"
                        height="80"
                    />
                </div>
            )}

            <div className="flex h-fit w-full flex-col gap-1">
                <span className="text-lg font-bold">{label}</span>
                <span className="text-md font-medium">{author}</span>
                <span className="text-md font-medium">
                    {parseDate}
                    {viewCount}
                </span>
            </div>
        </Link>
    );
};

export default CardBoard;
