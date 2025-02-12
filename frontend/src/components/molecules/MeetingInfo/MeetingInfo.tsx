// MeetingInfo.tsx
import type { TagProps } from '@/components/atoms/Tag/Tag';
import type { RatingStarProps } from '@/components/atoms/RatingStar/RatingStar';
import getDisplayedTags from '@/utils/filterTagsByLength';
import MeetingInfoView from './MeetingInfo.view';
import { customFetch } from '@/apis/customFetch';

export interface MeetingInfoProps {
    teamId: string;
    subTitle: string;
    rating: RatingStarProps;
    title: string;
    tag: TagProps[];
    maxCapacity: number;
    currentCapacity: number;
}

const MeetingInfo = ({
    subTitle,
    rating,
    title,
    tag,
    maxCapacity,
    currentCapacity,
    teamId,
}: MeetingInfoProps) => {
    const displayedTags = getDisplayedTags(tag);

    const joinapi = async (): Promise<void> => {
        const body = {
            teamId: teamId,
            nickname: '아무거나',
            notificationStatus: 'ACTIVE',
        };
        try {
            await customFetch('/team-user', {
                method: 'POST',
                credentials: 'include',
                body: JSON.stringify(body),
            });
            alert('가입신청');
        } catch (error) {
            console.error('Error fetching area teams:', error);
            throw error;
        }
    };

    const handleUpdateInfo = () => {
        console.log('Update Info button clicked');
        // 정보 수정 로직 구현 가능
    };

    const handleJoinRequest = () => {
        joinapi();
        console.log('Join Request button clicked');
    };

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
        />
    );
};

export default MeetingInfo;
