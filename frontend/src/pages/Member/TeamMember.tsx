import { useQuery } from '@tanstack/react-query';
import { useParams } from 'react-router-dom';
import { getTeamUsers } from '@/apis/TeamAPI';
import MemberList, {
    MemberListProps,
} from '@/components/molecules/MemberList/MemberList';

const TeamMember = () => {
    const { teamId } = useParams<{ teamId: string }>();

    const { data } = useQuery({
        queryKey: ['teamUsers', teamId],
        queryFn: () => getTeamUsers(teamId!),
        enabled: !!teamId,
    });

    // API 응답의 users 배열을 MemberListProps 형식으로 변환
    const members: MemberListProps[] =
        data?.users?.map((user) => ({
            role: user.role, // 들어온 role 그대로 사용
            userInfo: {
                userId: '', // 현재 값이 없으므로 빈 문자열
                userName: user.nickname,
                profileUri: '', // 프로필 이미지 정보 없음
            },
            bio: '', // 한 줄 소개 정보 없음
            joinDate: '', // 가입 날짜 정보 없음
        })) || [];

    // 앞에서부터 최대 3개만 표시 (3개보다 적으면 그대로 출력)
    const displayedMembers = members.slice(0, 3);

    const member = displayedMembers.map((item: MemberListProps) => (
        <MemberList
            bio={item.bio}
            role={item.role}
            key={item.userInfo.userId}
            userInfo={item.userInfo}
            joinDate={item.joinDate}
        />
    ));

    return <div className="flex min-h-[313px] flex-col gap-4">{member}</div>;
};

export default TeamMember;
