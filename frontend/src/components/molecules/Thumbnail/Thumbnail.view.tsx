import { MemberCount } from '@/components/atoms';

export interface ThumbnailProps {
    memberCount?: number;
    memberLimit?: number;
    imgSrc?: string;
    imgAlt?: string;
    showMember: boolean;
}

const ThumbnailView = ({
    memberCount = 0,
    memberLimit = 0,
    imgSrc,
    imgAlt,
    showMember,
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
            {showMember && (
                <MemberCount
                    memberCount={memberCount}
                    memberLimit={memberLimit}
                    addStyle="absolute bottom-4 right-3.5"
                />
            )}
        </div>
    );
};

export default ThumbnailView;
