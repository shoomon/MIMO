import { TeamUserRole } from '@/types/Team';
import type { ProfileImageProps } from './../../atoms/ProfileImage/ProfileImage';
import { ButtonDefault } from '@/components/atoms';
import { useState } from 'react';
import BasicModal from '../BasicModal/BasicModal';
import { useNavigate } from 'react-router-dom';
import { useQueryClient } from '@tanstack/react-query';

export interface MemberListViewProps {
    parsedDate?: string;
    userInfo: ProfileImageProps;
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

type ModalType =
    | 'editRole'
    | 'kickMember'
    | 'acceptMember'
    | 'rejectMember'
    | null;

const MemberListView = ({
    parsedDate,
    userInfo,
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
    // 어떤 모달이 열릴지 상태로 관리 (없으면 null)
    const [activeModal, setActiveModal] = useState<ModalType>(null);
    const closeModal = () => setActiveModal(null);
    const Navigate = useNavigate();
    const queryClient = useQueryClient();

    return (
        <div className="flex items-end justify-between">
            <div className="flex items-center gap-3">
                <div className="flex h-[60px] w-[60px] flex-shrink-0 items-center justify-center overflow-hidden rounded-lg">
                    <img
                        src={userInfo.profileUri}
                        alt="프로필 이미지"
                        className="h-full w-full object-cover"
                    />
                </div>
                <div className="flex flex-col justify-between">
                    <div className="flex items-center gap-1">
                        {userRoleElement}
                        <span className="text-lg font-medium">
                            {userInfo.nickname}
                        </span>
                    </div>
                    <div className="text-md font-normal">
                        {mode === 'member' && `가입일 ${parsedDate}`}
                    </div>
                </div>
            </div>

            {canPerformActions && (
                <div>
                    {mode === 'member' && (
                        <div className="flex gap-3">
                            {canPerformActions && targetRole !== 'LEADER' && (
                                <>
                                    <ButtonDefault
                                        type="default"
                                        content="권한 수정"
                                        onClick={() =>
                                            setActiveModal('editRole')
                                        }
                                    />
                                    <ButtonDefault
                                        type="fail"
                                        content="멤버 추방"
                                        onClick={() =>
                                            setActiveModal('kickMember')
                                        }
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
                                    onClick={() =>
                                        setActiveModal('acceptMember')
                                    }
                                />
                            )}
                            {onRejectMember && (
                                <ButtonDefault
                                    type="fail"
                                    content="거절"
                                    onClick={() =>
                                        setActiveModal('rejectMember')
                                    }
                                />
                            )}
                        </div>
                    )}
                </div>
            )}

            {/* 각 액션별 모달 렌더링 */}
            {activeModal === 'editRole' && (
                <BasicModal
                    isOpen={true}
                    title="권한 수정"
                    subTitle="멤버의 권한을 수정하시겠습니까?"
                    onConfirmClick={() => {
                        onEditRole?.();
                        closeModal();
                        Navigate(0);
                    }}
                    onCancelClick={closeModal}
                />
            )}
            {activeModal === 'kickMember' && (
                <BasicModal
                    isOpen={true}
                    title="멤버 추방"
                    subTitle="정말 멤버를 추방하시겠습니까?"
                    onDeleteClick={() => {
                        onKickMember?.();
                        closeModal();
                        queryClient.invalidateQueries({
                            queryKey: ['teamUsers'],
                        });
                    }}
                    onCancelClick={closeModal}
                />
            )}
            {activeModal === 'acceptMember' && (
                <BasicModal
                    isOpen={true}
                    title="가입 신청 승인"
                    subTitle="회원 가입 신청을 승인하시겠습니까?"
                    onConfirmClick={() => {
                        onAcceptMember?.();
                        closeModal();
                        queryClient.invalidateQueries({
                            queryKey: ['Invites'],
                        });
                    }}
                    onCancelClick={closeModal}
                />
            )}
            {activeModal === 'rejectMember' && (
                <BasicModal
                    isOpen={true}
                    title="가입 신청 거절"
                    subTitle="회원 가입 신청을 거절하시겠습니까?"
                    onDeleteClick={() => {
                        onRejectMember?.();
                        closeModal();
                        queryClient.invalidateQueries({
                            queryKey: ['Invites'],
                        });
                    }}
                    onCancelClick={closeModal}
                />
            )}
        </div>
    );
};

export default MemberListView;
