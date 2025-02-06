// MeetingInfoView.tsx
import { ButtonDefault } from '@/components/atoms';
import RatingStar, {
    RatingStarProps,
} from '@/components/atoms/RatingStar/RatingStar';
import type { MemberCountProps } from '@/components/atoms/MemberCount/MemberCount';

export interface MeetingInfoViewProps {
    subTitle: string;
    rating: RatingStarProps;
    title: string;
    displayedTags: React.ReactNode;
    member: MemberCountProps;
    onUpdateInfo: () => void;
    onJoinRequest: () => void;
}

const MeetingInfoView = ({
    subTitle,
    rating,
    title,
    displayedTags,
    member,
    onUpdateInfo,
    onJoinRequest,
}: MeetingInfoViewProps) => {
    return (
        <div className="flex w-full flex-col gap-1 px-16">
            <span className="text-md font-normal">{subTitle}</span>
            <RatingStar
                rating={rating.rating}
                reviewCount={rating.reviewCount}
            />
            <span className="text-display-xs font-extrabold">{title}</span>
            <div className="flex gap-2 py-1">{displayedTags}</div>
            <div className="flex items-center justify-between pt-4">
                <div className="text-xl font-medium text-gray-700">
                    멤버 {member.memberCount}/{member.memberLimit}
                </div>
                <div className="flex gap-3">
                    <ButtonDefault
                        type="default"
                        iconId="Pen"
                        content="정보 수정"
                        onClick={onUpdateInfo}
                    />
                    <ButtonDefault
                        type="primary"
                        iconId="Mail"
                        content="가입신청"
                        onClick={onJoinRequest}
                    />
                </div>
            </div>
        </div>
    );
};

export default MeetingInfoView;
