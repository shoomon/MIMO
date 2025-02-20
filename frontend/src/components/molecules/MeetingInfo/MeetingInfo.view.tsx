import { useState } from 'react';
import { ButtonDefault } from '@/components/atoms';
import RatingStar from '@/components/atoms/RatingStar/RatingStar';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';
import { TeamUserRole } from '@/types/Team';
import { Link, useParams, useNavigate } from 'react-router-dom';
import BasicInputModal from '@/components/molecules/BasicInputModal/BasicInputModal';
import BasicModal from '@/components/molecules/BasicModal/BasicModal';
import TagInputModal from '../BasicInputModal/TagInputModal';
import { updateNickname, addTag } from '@/apis/UserAPI';
import { getCategory, getArea } from '@/apis/TeamAPI';
import { useQuery } from '@tanstack/react-query';
import { TagsResponse } from '@/types/Team';

export interface MeetingInfoViewProps {
    subTitle: string;
    reviewScore: number;
    reviewCount: number;
    title: string;
    displayedTags: React.ReactNode;
    maxCapacity: number;
    currentCapacity: number;
    onUpdateInfo: () => void;
    onJoinRequest: () => void;
    onRemainReview: () => void;
    teamUserId: number | null;
    role: TeamUserRole;
    invited: boolean;
    hasReview: boolean;
}

const MeetingInfoView = ({
    subTitle,
    reviewScore,
    reviewCount,
    title,
    displayedTags,
    maxCapacity,
    currentCapacity,
    onUpdateInfo,
    onJoinRequest,
    role,
    invited,
    hasReview,
    onRemainReview,
}: MeetingInfoViewProps) => {
    const { teamId } = useParams() as { teamId: string };
    const navigate = useNavigate();

    const { data: myProfileData } = useMyTeamProfile(teamId);

    // 카테고리, 지역 목록 (서버에서 이미 존재하는 태그, 중복 체크)
    const { data: categoryData } = useQuery<TagsResponse>({
        queryKey: ['categoryList'],
        queryFn: getCategory,
    });
    const { data: areaData } = useQuery<TagsResponse>({
        queryKey: ['areaList'],
        queryFn: getArea,
    });

    // -----------------------
    // 닉네임 변경 모달 상태
    // -----------------------
    const [isNicknameModalOpen, setIsNicknameModalOpen] = useState(false);
    const [isSuccessModalOpen, setIsSuccessModalOpen] = useState(false);
    const [isErrorModalOpen, setIsErrorModalOpen] = useState(false);
    const [errorModalMessage, setErrorModalMessage] = useState('');

    // -----------------------
    // 태그 추가 모달 상태
    // -----------------------
    const [isTagModalOpen, setIsTagModalOpen] = useState(false);

    // 모달 외부(부모)에서 임시로 모은 태그
    const [pendingTags, setPendingTags] = useState<string[]>([]);

    // 태그 추가 성공/실패 모달
    const [isTagSuccessModalOpen, setIsTagSuccessModalOpen] = useState(false);
    const [isTagErrorModalOpen, setIsTagErrorModalOpen] = useState(false);
    const [tagErrorModalMessage, setTagErrorModalMessage] = useState('');

    // -----------------------
    // 닉네임 변경 로직
    // -----------------------
    const validateNickname = (nickname: string) => {
        const regex = /^[A-Za-z0-9가-힣]{1,30}$/;
        return regex.test(nickname);
    };

    const changeNickname = () => {
        setIsNicknameModalOpen(true);
    };

    const handleNicknameConfirm = (newNickname: string) => {
        if (!validateNickname(newNickname)) {
            setErrorModalMessage(
                '닉네임은 영문, 숫자, 한글로만 구성되며, 길이는 1~30자리 이하이어야 합니다.',
            );
            setIsErrorModalOpen(true);
            return;
        }
        updateNickname(teamId, newNickname)
            .then(() => {
                setIsNicknameModalOpen(false);
                setIsSuccessModalOpen(true);
            })
            .catch((error) => {
                setErrorModalMessage(error.message);
                setIsErrorModalOpen(true);
            });
    };

    // -----------------------
    // 태그 추가 로직
    // -----------------------
    const changeTag = () => {
        setIsTagModalOpen(true);
    };

    // 모달에서 "확인" 버튼으로 전달된 태그 배열 => 부모 pendingTags에 합침
    const handleTagModalConfirm = (tagsFromModal: string[]) => {
        // 이미 모달 내부에서 중복/유효성 검사를 했으므로 여기서는 바로 합치기만 해도 됨.
        setPendingTags((prev) => [...prev, ...tagsFromModal]);
        setIsTagModalOpen(false);
    };

    // "모두 저장" -> API
    const handleSavePendingTags = () => {
        if (pendingTags.length === 0) return;
        addTag(teamId, pendingTags)
            .then(() => {
                setPendingTags([]);
                setIsTagSuccessModalOpen(true);
            })
            .catch((error) => {
                setTagErrorModalMessage(error.message);
                setIsTagErrorModalOpen(true);
            });
    };

    return (
        <div className="flex w-full flex-col gap-1">
            {/* 상단 정보 */}
            <span className="text-md font-normal">{subTitle}</span>
            <Link to={'review'}>
                <RatingStar
                    reviewScore={reviewScore}
                    reviewCount={reviewCount}
                />
            </Link>
            <span className="text-display-xs font-extrabold">{title}</span>

            {/* 서버에 저장된 태그 (displayedTags) */}
            <div className="flex gap-2 py-1">
                {displayedTags ? displayedTags : '태그가 존재하지 않습니다.'}
            </div>

            <div className="flex items-center justify-between pt-4">
                <div className="text-xl font-medium text-gray-700">
                    멤버 {currentCapacity}/{maxCapacity}
                </div>
                <div className="flex items-stretch justify-center gap-2">
                    {/* 가입 신청 버튼 */}
                    {role === 'GUEST' && !invited && (
                        <ButtonDefault
                            type="primary"
                            iconId="Mail"
                            content="가입신청"
                            onClick={onJoinRequest}
                        />
                    )}
                    {role === 'LEADER' && (
                        <ButtonDefault
                            type="default"
                            iconId="Pen"
                            content="정보 수정"
                            onClick={onUpdateInfo}
                        />
                    )}
                    {role === 'GUEST' && invited && (
                        <ButtonDefault type="primary" content="신청중" />
                    )}
                    {/* 리뷰작성 */}
                    {role !== 'GUEST' && role !== 'LEADER' && !hasReview && (
                        <ButtonDefault
                            type="default"
                            iconId="Pen"
                            content="리뷰작성"
                            onClick={onRemainReview}
                        />
                    )}
                    {/* Member, CO_LEADER는 닉네임 변경 */}
                    {(myProfileData?.role === 'MEMBER' ||
                        myProfileData?.role === 'CO_LEADER') && (
                        <ButtonDefault
                            type="default"
                            content="닉네임 변경"
                            onClick={changeNickname}
                        />
                    )}
                </div>
            </div>

            {myProfileData?.role === 'LEADER' && (
                <div className="mt-2 flex items-center justify-end gap-2">
                    <ButtonDefault
                        type="default"
                        content="태그 추가"
                        onClick={changeTag}
                    />
                    <ButtonDefault
                        type="default"
                        content="닉네임 변경"
                        onClick={changeNickname}
                    />
                </div>
            )}

            {/* 아직 서버에 저장되지 않은 태그 목록 */}
            {pendingTags.length > 0 && (
                <div className="mt-4 flex flex-col gap-1 border-t border-gray-200 pt-2">
                    <span className="font-semibold text-red-600">
                        아직 저장되지 않은 태그:
                    </span>
                    <div className="flex flex-wrap gap-2">
                        {pendingTags.map((tag, idx) => (
                            <div
                                key={idx}
                                className="flex items-center gap-1 rounded bg-gray-200 px-2 py-1"
                            >
                                <span>{tag}</span>
                                <button
                                    type="button"
                                    onClick={() =>
                                        setPendingTags((prev) =>
                                            prev.filter((t) => t !== tag),
                                        )
                                    }
                                    className="text-red-500"
                                >
                                    X
                                </button>
                            </div>
                        ))}
                    </div>
                    <div className="mt-2 flex gap-2">
                        <ButtonDefault
                            type="primary"
                            content="모두 저장"
                            onClick={handleSavePendingTags}
                        />
                        <ButtonDefault
                            type="default"
                            content="취소"
                            onClick={() => setPendingTags([])}
                        />
                    </div>
                </div>
            )}

            {/* 닉네임 변경 모달 */}
            <BasicInputModal
                isOpen={isNicknameModalOpen}
                title="닉네임 변경"
                subTitle="새 닉네임을 입력해주세요."
                inputPlaceholder="새 닉네임"
                confirmLabel="닉네임 변경"
                onConfirmClick={handleNicknameConfirm}
                onCancelClick={() => setIsNicknameModalOpen(false)}
            />

            {/* 닉네임 변경 성공 모달 */}
            <BasicModal
                isOpen={isSuccessModalOpen}
                title="성공했습니다."
                subTitle="닉네임이 변경되었습니다."
                onConfirmClick={() => {
                    setIsSuccessModalOpen(false);
                    navigate(0);
                }}
            />

            {/* 닉네임 변경 실패 모달 */}
            <BasicModal
                isOpen={isErrorModalOpen}
                title="닉네임 변경 실패"
                subTitle={errorModalMessage}
                onConfirmClick={() => setIsErrorModalOpen(false)}
            />

            {/* 태그 추가 모달 (TagInputModal) */}
            <TagInputModal
                isOpen={isTagModalOpen}
                title="태그 추가"
                subTitle="새 태그를 입력 후 '추가' 버튼을 누르세요 (모달 내에서 즉시 검사)."
                inputPlaceholder="예: 요리"
                confirmLabel="확인"
                /* 모달에서 즉시 중복 체크를 위해 기존 태그 목록을 넘겨줌 */
                existingCategoryTags={categoryData?.tags || []}
                existingAreaTags={areaData?.tags || []}
                existingPendingTags={pendingTags}
                onConfirmClick={handleTagModalConfirm}
                onCancelClick={() => setIsTagModalOpen(false)}
            />

            {/* 태그 추가 성공 모달 */}
            <BasicModal
                isOpen={isTagSuccessModalOpen}
                title="성공했습니다."
                subTitle="태그가 추가되었습니다."
                onConfirmClick={() => {
                    setIsTagSuccessModalOpen(false);
                    navigate(0);
                }}
            />

            {/* 태그 추가 실패 모달 */}
            <BasicModal
                isOpen={isTagErrorModalOpen}
                title="태그 추가 실패"
                subTitle={tagErrorModalMessage}
                onConfirmClick={() => setIsTagErrorModalOpen(false)}
            />
        </div>
    );
};

export default MeetingInfoView;
