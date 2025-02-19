// MeetingInfoView.tsx
import { ButtonDefault } from '@/components/atoms';
import RatingStar from '@/components/atoms/RatingStar/RatingStar';
import { TeamUserRole } from '@/types/Team';
import { Link } from 'react-router-dom';

export interface MeetingInfoViewProps {
    subTitle: string;
    reviewScore: number;
    reviewCount: number;
    title: string;
    displayedTags: React.ReactNode;
    maxCapacity: number;
    currentCapacity: number;
    onUpdateInfo: () => void;
    onJoinRequest: () => void;
    onRemainReview: () => void;
    teamUserId: number | null;
    role: TeamUserRole;
    invited: boolean;
    hasReview: boolean;
}

const MeetingInfoView = ({
    subTitle,
    reviewScore,
    reviewCount,
    title,
    displayedTags,
    maxCapacity,
    currentCapacity,
    onUpdateInfo,
    onJoinRequest,
    onRemainReview,
    role,
    invited,
    hasReview,
}: MeetingInfoViewProps) => {
    return (
        <div className="flex w-full flex-col gap-1">
            <span className="text-md font-normal">{subTitle}</span>
            <Link to={'review'}>
                <RatingStar
                    reviewScore={reviewScore}
                    reviewCount={reviewCount}
                />
            </Link>
            <span className="text-display-xs font-extrabold">{title}</span>
            <div className="flex gap-2 py-1">
                {displayedTags ? displayedTags : '태그가 존재하지 않습니다.'}
            </div>
            <div className="flex items-center justify-between pt-4">
                <div className="text-xl font-medium text-gray-700">
                    멤버 {currentCapacity}/{maxCapacity}
                </div>
                <div className="flex gap-2">
                    {role === 'GUEST' && !invited && (
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
                    {role === 'GUEST' && invited && (
                        <ButtonDefault type="primary" content="신청중" />
                    )}
                    {role != 'GUEST' && role != 'LEADER' && !hasReview && (
                        <ButtonDefault
                            type="default"
                            iconId="Pen"
                            content="리뷰작성"
                            onClick={onRemainReview}
                        />
                    )}
                </div>
            </div>
        </div>
    );
};

export default MeetingInfoView;
