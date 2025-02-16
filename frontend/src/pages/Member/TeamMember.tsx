import { useQuery } from '@tanstack/react-query';
import { useParams } from 'react-router-dom';
import { getInvites, getTeamUsers } from '@/apis/TeamAPI';
import MemberList, {
    MemberListProps,
} from '@/components/molecules/MemberList/MemberList';
import BodyLayout_24 from '../layouts/BodyLayout_24';
import { Title } from '@/components/atoms';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';

const TeamMember = () => {
    const { teamId } = useParams() as { teamId: string };

    const { data } = useQuery({
        queryKey: ['teamUsers', teamId],
        queryFn: () => getTeamUsers(teamId!),
    });
    const { data: myProfileData } = useMyTeamProfile(teamId);

    const { data: inviteData } = useQuery({
        queryKey: ['Invites', teamId],
        queryFn: () => getInvites(teamId!),
        enabled: !(myProfileData?.role !== 'LEADER'), // 첫 번째 쿼리가 로딩 중이면 실행되지 않음
    });

    if (!myProfileData?.role) {
        return <div>?</div>;
    }

    // API 응답의 users 배열을 MemberListProps 형식으로 변환
    const members: MemberListProps[] =
        data?.users?.map((user) => ({
            role: user.role,
            userInfo: {
                userId: String(user.teamUserId),
                nickname: user.nickname,
                profileUri: user.profileUri,
                // 프로필 이미지 정보 없음
            },
            bio: 'user', // 한 줄 소개 정보 없음
            joinDate: user.joinTime, // 가입 날짜 정보 없음
            teamUserId: user.teamUserId,
        })) || [];

    // API 응답의 users 배열을 MemberListProps 형식으로 변환
    const invites: MemberListProps[] =
        inviteData?.teamInvites?.map((user) => ({
            userInfo: {
                userId: String(user.userId),
                nickname: user.name,
                profileUri: user.ProfileImage, // 프로필 이미지 정보 없음
            },
            bio: user.memo,
            teamInviteId: user.teamInviteId,
        })) || [];

    // LEADER인지 여부에 따라 렌더링 조건 결정
    const isLeader = myProfileData.role === 'LEADER';

    // LEADER면 최대 3개, 아니라면 전체 멤버를 표시
    const displayedMembers = isLeader ? members.slice(0, 3) : members;

    const displayedinvites = invites.slice(0, 3);
    console.log(displayedMembers);

    return (
        <>
            <div className="h-[51px]"></div>
            <BodyLayout_24>
                <section className="flex w-full flex-col gap-6">
                    <Title label="멤버" to="current" />
                    <div className="flex min-h-[313px] flex-col gap-4">
                        {displayedMembers.map((item: MemberListProps) => (
                            <MemberList
                                key={item.userInfo.userId}
                                role={item.role}
                                userInfo={item.userInfo}
                                bio={item.bio}
                                joinDate={item.joinDate}
                                teamUserId={item.teamUserId}
                            />
                        ))}
                    </div>
                </section>
                {isLeader && (
                    <section className="flex w-full flex-col gap-6">
                        <Title label="가입 신청" to="addition" />
                        <div className="flex min-h-[313px] flex-col gap-4">
                            {displayedinvites.map((item: MemberListProps) => (
                                <MemberList
                                    key={item.userInfo.userId}
                                    userInfo={item.userInfo}
                                    bio={item.bio}
                                    joinDate={item.joinDate}
                                    teamInviteId={item.teamInviteId}
                                />
                            ))}
                        </div>
                    </section>
                )}
            </BodyLayout_24>
        </>
    );
};

export default TeamMember;
