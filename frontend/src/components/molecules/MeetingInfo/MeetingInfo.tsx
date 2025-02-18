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

type ModalType = 'public' | 'private' | null;

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
        if (recruitStatus === 'ACTIVE_PUBLIC') {
            // 공개 가입: 닉네임 입력 모달 열기
            openModal('public');
        } else if (recruitStatus === 'ACTIVE_PRIVATE') {
            // 비공개 가입: 메모 입력 모달 열기
            openModal('private');
        } else {
            throw new Error('가입할 수 없는 상태입니다.');
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
            />
            {isModalOpen && modalType === 'public' && (
                <BasicInputModal
                    confirmLabel="가입하기"
                    isOpen={isModalOpen}
                    title="닉네임 입력"
                    subTitle="가입할 때 사용할 닉네임을 입력하세요"
                    onConfirmClick={(inputNickName) => {
                        // 입력받은 닉네임을 이용해 공개 가입 요청
                        joinTeamForPublic(
                            teamId,
                            inputNickName,
                            notificationStatus,
                        );
                        closeModal();
                    }}
                    onCancelClick={closeModal}
                />
            )}
            {isModalOpen && modalType === 'private' && (
                <BasicInputModal
                    confirmLabel="가입하기"
                    isOpen={isModalOpen}
                    title="메모 입력"
                    subTitle="가입 시 사용할 메모를 입력하세요"
                    onConfirmClick={(inputMemo) => {
                        // 입력받은 메모를 이용해 비공개 가입 요청
                        joinTeamForPrivate(teamId, inputMemo);
                        closeModal();
                    }}
                    onCancelClick={closeModal}
                />
            )}
        </>
    );
};

export default MeetingInfo;
