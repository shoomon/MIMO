import { dateParsing } from '@/utils';
import type { ProfileImageProps } from './../../atoms/ProfileImage/ProfileImage';
import { Icon } from '@/components/atoms';
import MemberListView from './MemberList.view';
import { TeamUserRole } from '@/types/Team';
import {
    acceptMember,
    deleteUsers,
    downgradeRole,
    rejectMember,
    upgradeRole,
} from '@/apis/TeamAPI';
import { useParams } from 'react-router-dom';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';

/**
 * 멤버 목록 컴포넌트의 props 타입 정의
 */
export interface MemberListProps {
    /** 사용자의 역할 ('LEADER' | 'CO_LEADER' | 'MEMBER') */
    role?: TeamUserRole;
    /** 사용자의 프로필 정보 */
    userInfo: ProfileImageProps;
    /** 사용자 소개 (한 줄 소개) */
    bio: string;
    /** 가입 날짜 (ISO 8601 형식) */
    joinDate?: string;
    teamInviteId?: number;
    teamUserId?: number;
}

/**
 * 회원 목록(팀원)과 회원 신청 목록(가입 신청)을 모두 렌더링하는 컴포넌트
 */
const MemberList = ({
    role,
    userInfo,
    bio,
    joinDate,
    teamInviteId,
    teamUserId,
}: MemberListProps): JSX.Element => {
    const parsedDate = joinDate
        ? dateParsing(new Date(joinDate), true)
        : undefined;

    const { teamId } = useParams<{ teamId: string }>();
    const { data: myProfileData } = useMyTeamProfile(teamId);

    if (!teamId) {
        return <div>teamId가 없습니다.</div>;
    }

    // 현재 접속한 사용자의 역할이 LEADER인지 체크 (권한이 있어야 버튼 렌더링)
    const isLeader = myProfileData?.role === 'LEADER';

    // 역할에 따른 아이콘 및 라벨 렌더링 함수
    const renderUserRoleElement = (role: string): JSX.Element => {
        if (role === 'LEADER') {
            return (
                <div className="flex items-center gap-1">
                    <Icon type="svg" id="Crown" className="mb-1" />
                    <span className="flex items-center text-lg font-extrabold">
                        모임장
                    </span>
                </div>
            );
        }
        if (role === 'CO_LEADER') {
            return (
                <div className="flex items-center gap-1">
                    <Icon type="svg" id="Medal" />
                    <span className="flex items-center text-lg font-extrabold">
                        운영진
                    </span>
                </div>
            );
        }
        return <span className="text-lg font-extrabold">멤버</span>;
    };

    // 모드 및 액션 핸들러 정의
    // mode가 'member'이면 팀원 목록, 'invite'이면 가입 신청 목록으로 인식
    let mode: 'member' | 'invite' = 'member';
    let onEditRole: (() => void) | undefined;
    let onKickMember: (() => void) | undefined;
    let onAcceptMember: (() => void) | undefined;
    let onRejectMember: (() => void) | undefined;

    if (teamUserId) {
        mode = 'member';
        if (isLeader) {
            onEditRole = () => {
                // 대상 멤버의 역할에 따라 권한 수정: CO_LEADER는 다운그레이드, MEMBER는 업그레이드
                if (role === 'CO_LEADER') {
                    downgradeRole(teamId, teamUserId);
                } else if (role === 'MEMBER') {
                    upgradeRole(teamId, teamUserId);
                }
            };
            onKickMember = () => {
                if (!userInfo.userId) return;
                deleteUsers(teamId, teamUserId);
            };
        }
    } else if (teamInviteId) {
        mode = 'invite';
        if (isLeader) {
            onAcceptMember = () => {
                acceptMember(teamId, teamInviteId);
            };
            onRejectMember = () => {
                rejectMember(teamId, teamInviteId);
            };
        }
    }

    // 회원 신청인 경우 role 정보가 없으므로 버튼 위쪽에는 "회원 신청" 텍스트 표시
    const userRoleElement = teamInviteId ? (
        <span className="text-lg font-extrabold">회원 신청</span>
    ) : (
        renderUserRoleElement(role || 'MEMBER')
    );

    return (
        <MemberListView
            parsedDate={parsedDate}
            userInfo={userInfo}
            bio={bio}
            userRoleElement={userRoleElement}
            mode={mode}
            targetRole={role || 'MEMBER'}
            onEditRole={onEditRole}
            onKickMember={onKickMember}
            onAcceptMember={onAcceptMember}
            onRejectMember={onRejectMember}
            currentUserRole={myProfileData?.role || 'GUEST'}
        />
    );
};

export default MemberList;
