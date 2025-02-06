// MeetingInfo.tsx
import type { TagProps } from '@/components/atoms/Tag/Tag';
import type { RatingStarProps } from '@/components/atoms/RatingStar/RatingStar';
import type { MemberCountProps } from '@/components/atoms/MemberCount/MemberCount';
import getDisplayedTags from '@/utils/filterTagsByLength';
import MeetingInfoView from './MeetingInfo.view';

export interface MeetingInfoProps {
    subTitle: string;
    rating: RatingStarProps;
    title: string;
    tag: TagProps[];
    member: MemberCountProps;
}

const MeetingInfo = ({
    subTitle,
    rating,
    title,
    tag,
    member,
}: MeetingInfoProps) => {
    const displayedTags = getDisplayedTags(tag);

    const handleUpdateInfo = () => {
        console.log('Update Info button clicked');
        // 정보 수정 로직 구현 가능
    };

    const handleJoinRequest = () => {
        console.log('Join Request button clicked');
        // 가입 신청 로직 구현 가능
    };

    return (
        <MeetingInfoView
            subTitle={subTitle}
            rating={rating}
            title={title}
            displayedTags={displayedTags}
            member={member}
            onUpdateInfo={handleUpdateInfo}
            onJoinRequest={handleJoinRequest}
        />
    );
};

export default MeetingInfo;
