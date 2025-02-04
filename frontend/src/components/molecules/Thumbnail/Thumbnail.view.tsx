import { MemberCount } from '@/components/atoms';

export interface ThumbnailProps {
    memberCount: number;
    memberLimit: number;
    memberClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
    imgSrc?: string;
    imgAlt?: string;
}

const ThumbnailView = ({
    memberCount,
    memberLimit,
    memberClick,
    imgSrc,
    imgAlt,
}: ThumbnailProps) => {
    return (
        <div className="relative flex h-[11.25rem] w-[21.5rem] flex-col items-center justify-end rounded-2xl bg-gray-200">
            {imgSrc === undefined ? (
                <span className="my-4 text-gray-500">사진이 없습니다.</span>
            ) : (
                <img
                    src={imgSrc}
                    alt={imgAlt}
                    className="h-[11.25rem] w-[21.5rem] object-cover"
                />
            )}
            <MemberCount
                memberCount={memberCount}
                memberLimit={memberLimit}
                onClick={memberClick}
                addStyle="absolute bottom-4 right-3.5"
            />
        </div>
    );
};

export default ThumbnailView;
