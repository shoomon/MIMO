import { TeamUserRole } from '@/types/Team';
import type { ProfileImageProps } from './../../atoms/ProfileImage/ProfileImage';
import { ButtonDefault } from '@/components/atoms';

export interface MemberListViewProps {
    parsedDate?: string;
    userInfo: ProfileImageProps;
    bio: string;
    // 역할 아이콘 및 라벨 요소 (ex. 모임장, 운영진, 멤버 또는 회원 신청)
    userRoleElement: React.ReactNode;
    // 렌더링 모드: 팀원(member) 또는 가입 신청(invite)
    mode: 'member' | 'invite';
    // 회원 목록일 때 대상 멤버의 역할
    targetRole?: TeamUserRole;
    // 팀원 액션 핸들러 (회원 목록)
    onEditRole?: () => void;
    onKickMember?: () => void;
    // 회원 신청 액션 핸들러 (가입 신청 목록)
    onAcceptMember?: () => void;
    onRejectMember?: () => void;
    // 현재 접속한 사용자의 역할
    currentUserRole: TeamUserRole;
}

const MemberListView = ({
    parsedDate,
    userInfo,
    bio,
    userRoleElement,
    mode,
    targetRole,
    onEditRole,
    onKickMember,
    onAcceptMember,
    onRejectMember,
    currentUserRole,
}: MemberListViewProps) => {
    // 현재 접속한 사용자가 LEADER일 때만 액션 버튼 렌더링
    const canPerformActions = currentUserRole === 'LEADER';

    return (
        <div className="flex items-end justify-between">
            <div className="flex items-center gap-3">
                <div className="flex h-[84px] w-[84px] flex-shrink-0 items-center justify-center overflow-hidden rounded-2xl">
                    <img
                        src={userInfo.profileUri}
                        alt="프로필 이미지"
                        className="h-full w-full object-cover"
                    />
                </div>
                <div className="flex flex-col gap-1">
                    <div className="flex items-center gap-1">
                        {userRoleElement}
                        <span className="text-lg font-medium">
                            {userInfo.nickname}
                        </span>
                    </div>
                    <span className="text-md font-medium">{bio}</span>
                    <span className="text-md font-normal">
                        가입일 {parsedDate}
                    </span>
                </div>
            </div>
            {canPerformActions && (
                <div>
                    {mode === 'member' && (
                        <div className="flex gap-3">
                            {canPerformActions &&
                                !(targetRole === 'LEADER') && (
                                    <>
                                        <ButtonDefault
                                            type="default"
                                            content="권한 수정"
                                            onClick={onEditRole}
                                        />
                                        <ButtonDefault
                                            type="fail"
                                            content="멤버 추방"
                                            onClick={onKickMember}
                                        />
                                    </>
                                )}
                        </div>
                    )}
                    {mode === 'invite' && (
                        <div className="flex gap-3">
                            {onAcceptMember && (
                                <ButtonDefault
                                    type="default"
                                    content="승인"
                                    onClick={onAcceptMember}
                                />
                            )}
                            {onRejectMember && (
                                <ButtonDefault
                                    type="fail"
                                    content="거절"
                                    onClick={onRejectMember}
                                />
                            )}
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default MemberListView;
