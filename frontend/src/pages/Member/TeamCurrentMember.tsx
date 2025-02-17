import { Title } from '@/components/atoms';
import BodyLayout_24 from '../layouts/BodyLayout_24';
import { useQuery } from '@tanstack/react-query';
import { useParams } from 'react-router-dom';
import { getTeamUsers } from '@/apis/TeamAPI';
import MemberList, {
    MemberListProps,
} from '@/components/molecules/MemberList/MemberList';

const TeamCurrentMember = () => {
    const { teamId } = useParams<{ teamId: string }>();

    const { data } = useQuery({
        queryKey: ['teamUsers', teamId],
        queryFn: () => getTeamUsers(teamId!),
        enabled: !!teamId,
    });

    const members: MemberListProps[] =
        data?.users?.map((user) => ({
            role: user.role, // 들어온 role 그대로 사용
            userInfo: {
                userId: String(user.teamUserId), // 현재 값이 없으므로 빈 문자열
                nickname: user.nickname,
                profileUri: user.profileUri, // 프로필 이미지 정보 없음
            },
            bio: '', // 한 줄 소개 정보 없음
            joinDate: user.joinTime, // 가입 날짜 정보 없음
        })) || [];

    const member = members.map((item: MemberListProps) => (
        <MemberList
            bio={item.bio}
            role={item.role}
            key={item.userInfo.userId}
            userInfo={item.userInfo}
            joinDate={item.joinDate}
        />
    ));
    return (
        <BodyLayout_24>
            <section className="flex w-full flex-col gap-6">
                <Title label="멤버 목록" />
                <>{member}</>
            </section>
        </BodyLayout_24>
    );
};

export default TeamCurrentMember;
