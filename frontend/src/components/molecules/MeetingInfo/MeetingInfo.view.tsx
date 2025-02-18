// MeetingInfoView.tsx
import { ButtonDefault } from '@/components/atoms';
import RatingStar, {
    RatingStarProps,
} from '@/components/atoms/RatingStar/RatingStar';
import { TeamUserRole } from '@/types/Team';

export interface MeetingInfoViewProps {
    subTitle: string;
    rating: RatingStarProps;
    title: string;
    displayedTags: React.ReactNode;
    maxCapacity: number;
    currentCapacity: number;
    onUpdateInfo: () => void;
    onJoinRequest: () => void;
    teamUserId: number | null;
    role: TeamUserRole;
}

const MeetingInfoView = ({
    subTitle,
    rating,
    title,
    displayedTags,
    maxCapacity,
    currentCapacity,
    onUpdateInfo,
    onJoinRequest,
    role,
}: MeetingInfoViewProps) => {
    return (
        <div className="flex w-full flex-col gap-1">
            <span className="text-md font-normal">{subTitle}</span>
            <RatingStar rating={rating.rating} />
            <span className="text-display-xs font-extrabold">{title}</span>
            <div className="flex gap-2 py-1">
                {displayedTags ? displayedTags : '태그가 존재하지 않습니다.'}
            </div>
            <div className="flex items-center justify-between pt-4">
                <div className="text-xl font-medium text-gray-700">
                    멤버 {currentCapacity}/{maxCapacity}
                </div>
                <div className="flex gap-2">
                    {role === 'GUEST' && (
                        <ButtonDefault
                            type="primary"
                            iconId="Mail"
                            content="가입신청"
                            onClick={onJoinRequest}
                        />
                    )}
                    {role === 'LEADER' && (
                        <ButtonDefault
                            type="default"
                            iconId="Pen"
                            content="정보 수정"
                            onClick={onUpdateInfo}
                        />
                    )}
                </div>
            </div>
        </div>
    );
};

export default MeetingInfoView;
