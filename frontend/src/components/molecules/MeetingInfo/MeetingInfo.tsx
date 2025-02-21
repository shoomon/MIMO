// MeetingInfo.tsx
import { useState } from 'react';
import type { TagProps } from '@/components/atoms/Tag/Tag';
import getDisplayedTags from '@/utils/filterTagsByLength';
import MeetingInfoView from './MeetingInfo.view';
import { joinTeamForPrivate, joinTeamForPublic } from '@/apis/TeamAPI';
import { TeamNotificationStatus, TeamRecruitStatus } from '@/types/Team';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';
import { useNavigate } from 'react-router-dom';
import BasicInputModal from '../BasicInputModal/BasicInputModal';
import { useAuth, useOauth } from '@/hooks/useAuth';
import BasicModal from '../BasicModal/BasicModal';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import ReviewInputModal from '../BasicInputModal/ReviewInputModal';
import { remainTeamReview } from '@/apis/UserAPI';

export interface MeetingInfoProps {
    teamId: string;
    reviewScore: number;
    reviewCount: number;
    title: string;
    tag: TagProps[];
    maxCapacity: number;
    currentCapacity: number;
    teamUserId: number | null;
    recruitStatus: TeamRecruitStatus;
    notificationStatus: TeamNotificationStatus;
}

type ModalType =
    | 'public'
    | 'private'
    | 'kick'
    | 'accept'
    | 'ready'
    | 'review'
    | null;

const MeetingInfo = ({
    title,
    tag,
    maxCapacity,
    currentCapacity,
    teamId,
    teamUserId,
    recruitStatus,
    notificationStatus,
    reviewScore,
    reviewCount,
}: MeetingInfoProps) => {
    const displayedTags = getDisplayedTags(tag);
    const navigate = useNavigate();
    const { userInfo } = useAuth();
    const queryClient = useQueryClient();
    const [isConfirmDisabled, setIsConfirmDisabled] = useState(false);
    const { handleLogin } = useOauth();
    const SendJoinRequest = useMutation({
        mutationFn: (inputMemo: string) =>
            joinTeamForPrivate(teamId, inputMemo),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['teamInfo', teamId] });
            queryClient.invalidateQueries({ queryKey: ['MyInfo', teamId] });
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
            queryClient.invalidateQueries({ queryKey: ['teamInfo', teamId] });
            queryClient.invalidateQueries({ queryKey: ['MyInfo', teamId] });
            setModalType('accept');
        },
        onError: (error) => {
            console.error('가입 신청', error);
            alert('가입 신청에 실패했습니다.');
        },
    });

    // 모달 상태 관리
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

    const login = () => {
        handleLogin();
        setIsModalOpen(false);
    };

    const handleUpdateInfo = () => {
        navigate(`/team/${teamId}/edit`);
    };

    // onRemainReview: 리뷰 입력 모달 열기
    const onRemainReview = () => {
        openModal('review');
    };

    const handleJoinRequest = () => {
        if (userInfo == undefined) {
            openModal('kick');
        } else if (recruitStatus === 'ACTIVE_PRIVATE') {
            openModal('private');
        } else if (recruitStatus === 'ACTIVE_PUBLIC') {
            openModal('public');
        }
    };

    const { data: myProfileData } = useMyTeamProfile(teamId);
    if (!myProfileData) return null;

    return (
        <>
            <MeetingInfoView
                reviewScore={reviewScore}
                reviewCount={reviewCount}
                title={title}
                displayedTags={displayedTags}
                maxCapacity={maxCapacity}
                currentCapacity={currentCapacity}
                onUpdateInfo={handleUpdateInfo}
                onJoinRequest={handleJoinRequest}
                onRemainReview={onRemainReview}
                teamUserId={teamUserId}
                role={myProfileData.role}
                invited={myProfileData.isInvited}
                hasReview={myProfileData.hasReview}
            />

            {isModalOpen && modalType === 'public' && (
                <BasicInputModal
                    confirmLabel="가입하기"
                    isOpen={isModalOpen}
                    title="닉네임 입력"
                    subTitle="모임에서 사용할 닉네임을 입력하세요"
                    onConfirmClick={(inputNickName) => {
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
                    subTitle="확인을 누르면 로그인 페이지가 열립니다."
                    onConfirmClick={login}
                />
            )}

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

            {isModalOpen && modalType === 'review' && (
                <ReviewInputModal
                    confirmLabel="리뷰 등록"
                    isOpen={isModalOpen}
                    title="리뷰 작성"
                    subTitle="리뷰 내용을 입력하고 평점을 작성하세요."
                    confirmDisabled={isConfirmDisabled} // 버튼 disabled prop 전달
                    onConfirmClick={({ reviewText, rating }) => {
                        // 버튼 클릭 시 바로 disabled 상태로 전환
                        setIsConfirmDisabled(true);

                        remainTeamReview(teamId, reviewText, rating.toString())
                            .then(() => {
                                queryClient.invalidateQueries({
                                    queryKey: ['teamInfo', teamId],
                                });
                                queryClient.invalidateQueries({
                                    queryKey: ['MyInfo', teamId],
                                });
                                closeModal();
                            })
                            .catch((error) => {
                                console.error('리뷰 등록 실패', error);
                                alert('리뷰 등록에 실패했습니다.');
                            })
                            .finally(() => {
                                // 예를 들어 1초 딜레이 후 다시 버튼 활성화
                                setTimeout(() => {
                                    setIsConfirmDisabled(false);
                                }, 1000);
                            });
                    }}
                    onCancelClick={closeModal}
                />
            )}
        </>
    );
};

export default MeetingInfo;
