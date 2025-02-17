import { getInvites } from '@/apis/TeamAPI';
import MemberList, {
    MemberListProps,
} from '@/components/molecules/MemberList/MemberList';
import { useQuery } from '@tanstack/react-query';
import { useParams } from 'react-router-dom';
import BodyLayout_24 from '../layouts/BodyLayout_24';
import { Title } from '@/components/atoms';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';

const TeamAdditionMember = () => {
    const { teamId } = useParams<{ teamId: string }>();
    const { data: myProfileData } = useMyTeamProfile(teamId);
    const { data: inviteData } = useQuery({
        queryKey: ['Invites', teamId],
        queryFn: () => getInvites(teamId!),
        enabled: !(myProfileData?.role !== 'LEADER'), // 첫 번째 쿼리가 로딩 중이면 실행되지 않음
    });

    if (!myProfileData?.role) {
        return <div>?</div>;
    }

    const invites: MemberListProps[] =
        inviteData?.teamInvites?.map((user) => ({
            userInfo: {
                userId: String(user.userId),
                nickname: user.name,
                profileUri: user.profileUri, // 프로필 이미지 정보 없음
            },
            bio: user.memo,
            teamInviteId: user.teamInviteId,
        })) || [];

    return (
        <BodyLayout_24>
            <section className="flex w-full flex-col gap-6">
                <Title label="신청 목록" />
                {invites.map((item: MemberListProps) => (
                    <MemberList
                        key={item.userInfo.userId}
                        userInfo={item.userInfo}
                        bio={item.bio}
                        joinDate={item.joinDate}
                        teamInviteId={item.teamInviteId}
                    />
                ))}
            </section>
        </BodyLayout_24>
    );
};

export default TeamAdditionMember;
