// MeetingInfo.tsx
import { useState } from 'react';
import type { TagProps } from '@/components/atoms/Tag/Tag';
import type { RatingStarProps } from '@/components/atoms/RatingStar/RatingStar';
import getDisplayedTags from '@/utils/filterTagsByLength';
import MeetingInfoView from './MeetingInfo.view';
import { joinTeamForPrivate, joinTeamForPublic } from '@/apis/TeamAPI';
import { TeamNotificationStatus, TeamRecruitStatus } from '@/types/Team';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';
import { useNavigate } from 'react-router-dom';
import BasicInputModal from '../BasicInputModal/BasicInputModal';
import { useAuth } from '@/hooks/useAuth';
import BasicModal from '../BasicModal/BasicModal';
import { useMutation, useQueryClient } from '@tanstack/react-query';

export interface MeetingInfoProps {
    teamId: string;
    subTitle: string;
    rating: RatingStarProps;
    title: string;
    tag: TagProps[];
    maxCapacity: number;
    currentCapacity: number;
    teamUserId: number | null;
    recruitStatus: TeamRecruitStatus;
    notificationStatus: TeamNotificationStatus;
}

type ModalType = 'public' | 'private' | 'kick' | 'accept' | 'ready' | null;

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
    notificationStatus,
}: MeetingInfoProps) => {
    const displayedTags = getDisplayedTags(tag);
    const navigate = useNavigate();
    const { userInfo } = useAuth();
    const queryClient = useQueryClient();

    const SendJoinRequest = useMutation({
        mutationFn: (inputMemo: string) =>
            joinTeamForPrivate(teamId, inputMemo),
        onSuccess: () => {
            // 삭제 성공 시 캐시 정리 (필요하다면)
            queryClient.invalidateQueries({
                queryKey: ['teamInfo', teamId],
            });
            queryClient.invalidateQueries({
                queryKey: ['MyInfo', teamId],
            });
        },
        onError: (error) => {
            console.error('가입 신청', error);
            alert('가입 신청에 실패했습니다.');
        },
    });

    const JoinImmediately = useMutation({
        mutationFn: (inputNickName: string) =>
            joinTeamForPublic(teamId, inputNickName, notificationStatus),
        onSuccess: () => {
            // 삭제 성공 시 캐시 정리 (필요하다면)

            queryClient.invalidateQueries({
                queryKey: ['teamInfo', teamId],
            });
            queryClient.invalidateQueries({
                queryKey: ['MyInfo', teamId],
            });
            setModalType('accept');
        },
        onError: (error) => {
            console.error('가입 신청', error);
            alert('가입 신청에 실패했습니다.');
        },
    });

    // 모달 상태 관리: modalType에 따라 어떤 모달을 보여줄지 결정
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [modalType, setModalType] = useState<ModalType>(null);
    const openModal = (type: ModalType) => {
        setModalType(type);
        setIsModalOpen(true);
    };
    const closeModal = () => {
        setModalType(null);
        setIsModalOpen(false);
    };

    const handleUpdateInfo = () => {
        navigate(`/team/${teamId}/edit`);
    };

    const handleJoinRequest = () => {
        if (userInfo == undefined) {
            openModal('kick');
        } else if (recruitStatus === 'ACTIVE_PRIVATE') {
            // 비공개 가입: 메모 입력 모달 열기
            openModal('private');
        } else if (recruitStatus === 'ACTIVE_PUBLIC') {
            // 공개 가입: 닉네임 입력 모달 열기
            openModal('public');
        }
    };

    const { data: myProfileData } = useMyTeamProfile(teamId);
    if (!myProfileData) return null;

    return (
        <>
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
                invited={myProfileData.isInvited}
            />
            {isModalOpen && modalType === 'public' && (
                <BasicInputModal
                    confirmLabel="가입하기"
                    isOpen={isModalOpen}
                    title="닉네임 입력"
                    subTitle="모임에서 사용할 닉네임을 입력하세요"
                    onConfirmClick={(inputNickName) => {
                        // 입력받은 닉네임을 이용해 공개 가입 요청
                        JoinImmediately.mutate(inputNickName);
                    }}
                    onCancelClick={closeModal}
                />
            )}
            {isModalOpen && modalType === 'private' && (
                <BasicInputModal
                    confirmLabel="가입신청"
                    isOpen={isModalOpen}
                    title="승인 후 가입되는 모임입니다."
                    subTitle="모임장에게 자신을 알려주세요."
                    onConfirmClick={(inputMemo) => {
                        // 입력받은 메모를 이용해 비공개 가입 요청
                        SendJoinRequest.mutate(inputMemo);
                        closeModal();
                    }}
                    onCancelClick={closeModal}
                />
            )}
            {isModalOpen && modalType === 'kick' && (
                <BasicModal
                    isOpen={isModalOpen}
                    title="로그인이 필요합니다."
                    subTitle="확인을 누르면 로그인 페이지로 이동합니다."
                    onConfirmClick={() => {
                        navigate('/login');
                    }}
                />
            )}{' '}
            {isModalOpen && modalType === 'accept' && (
                <BasicModal
                    isOpen={isModalOpen}
                    title="가입 성공!"
                    subTitle="이제 모임에서 활동할 수 있어요"
                    onConfirmClick={() => {
                        closeModal();
                    }}
                />
            )}
            {isModalOpen && modalType === 'ready' && (
                <BasicModal
                    isOpen={isModalOpen}
                    title="가입 신청 성공!"
                    subTitle="모임장의 승인 후 가입됩니다."
                    onConfirmClick={() => {
                        closeModal();
                    }}
                />
            )}
        </>
    );
};

export default MeetingInfo;
