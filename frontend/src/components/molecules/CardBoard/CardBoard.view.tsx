// CardBoardView.tsx
import { Link } from 'react-router-dom';
import Thumbnail from '../Thumbnail/Thumbnail';
import { layoutType } from '@/types/TeamBoard';

export interface CardBoardViewProps {
    userProfileUri: string;
    userNickname: string;
    postTitle: string;
    imageUri: string;
    likeCount: number;
    viewCount: number;
    createdAt: string;
    updatedAt: string;
    commentCount: number;
    layoutType: layoutType;
    linkto: string;
}

const CardBoardView: React.FC<CardBoardViewProps> = ({
    userProfileUri,
    userNickname,
    postTitle,
    imageUri,
    likeCount,
    viewCount,
    createdAt,
    commentCount,
    layoutType,
    linkto,
}) => {
    return (
        <Link
            to={linkto}
            className={`${
                layoutType === 'Card'
                    ? 'flex h-fit w-[344px] flex-col overflow-hidden rounded-t-2xl'
                    : 'flex items-center'
            } gap-3`}
        >
            {layoutType === 'Card' ? (
                <Thumbnail
                    imgSrc={imageUri}
                    imgAlt="게시글썸네일"
                    showMember={false}
                />
            ) : (
                <div className="flex h-[84px] w-[84px] flex-shrink-0 items-center justify-center overflow-hidden rounded-2xl">
                    <img
                        src={imageUri}
                        alt="게시글썸네일"
                        className="h-full w-full object-cover"
                    />
                </div>
            )}

            <div className="flex h-fit w-full flex-col gap-1 px-1 pb-1">
                <div className="flex items-center">
                    <span className="flex-1 truncate text-lg font-extrabold">
                        {postTitle}
                    </span>
                    <span className="flex-shrink-0 font-extrabold whitespace-nowrap">
                        [{commentCount}]
                    </span>
                </div>
                <div>
                    <div className="flex items-center">
                        <img
                            src={userProfileUri}
                            alt="User Profile"
                            className="h-5 w-5 rounded-md"
                        />
                        <span className="text-md font-semibold">
                            {userNickname}
                        </span>
                    </div>
                </div>
                <span className="text-sm font-light">{createdAt}</span>

                <div className="flex gap-3 text-sm font-medium">
                    <span>조회수 {viewCount}</span>
                    <span>댓글수 {commentCount}</span>
                    <span>좋아요 {likeCount}개</span>
                </div>
            </div>
        </Link>
    );
};

export default CardBoardView;
