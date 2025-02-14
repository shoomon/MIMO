// MeetingInfo.tsx
import type { TagProps } from '@/components/atoms/Tag/Tag';
import type { RatingStarProps } from '@/components/atoms/RatingStar/RatingStar';
import getDisplayedTags from '@/utils/filterTagsByLength';
import MeetingInfoView from './MeetingInfo.view';
import { joinTeamForPrivate, joinTeamForPublic } from '@/apis/TeamAPI';
import { TeamNotificationStatus, TeamRecruitStatus } from '@/types/Team';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';

export interface MeetingInfoProps {
    teamId: string;
    subTitle: string;
    rating: RatingStarProps;
    title: string;
    tag: TagProps[];
    maxCapacity: number;
    currentCapacity: number;
    teamUserId: number | null;
    nickName: string;
    recruitStatus: TeamRecruitStatus;
    notificationStatus: TeamNotificationStatus;
}

const MeetingInfo = ({
    subTitle,
    rating,
    title,
    tag,
    maxCapacity,
    currentCapacity,
    teamId,
    teamUserId,
    recruitStatus,
    nickName,
    notificationStatus,
}: MeetingInfoProps) => {
    const displayedTags = getDisplayedTags(tag);

    const handleUpdateInfo = () => {
        console.log('Update Info button clicked');
        // 정보 수정 로직 구현 가능
    };

    const handleJoinRequest = () => {
        if (recruitStatus === 'ACTIVE_PUBLIC') {
            joinTeamForPublic(teamId, nickName, notificationStatus);
        } else if (recruitStatus === 'ACTIVE_PRIVATE') {
            let memo = window.prompt('메모를 입력하세요', '');
            if (!memo || memo.trim() === '') {
                memo = '안녕하세요? 잘 부탁드립니다.';
            }
            joinTeamForPrivate(teamId, memo);
        } else {
            throw new Error('이런');
        }
    };
    const { data: myProfileData } = useMyTeamProfile(teamId);

    if (!myProfileData) {
        return;
    }
    return (
        <MeetingInfoView
            subTitle={subTitle}
            rating={rating}
            title={title}
            displayedTags={displayedTags}
            maxCapacity={maxCapacity}
            currentCapacity={currentCapacity}
            onUpdateInfo={handleUpdateInfo}
            onJoinRequest={handleJoinRequest}
            teamUserId={teamUserId}
            role={myProfileData.role}
        />
    );
};

export default MeetingInfo;
