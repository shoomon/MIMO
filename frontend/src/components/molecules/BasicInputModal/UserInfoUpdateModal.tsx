import { useState, useEffect } from 'react';
import { ButtonPrimary } from '@/components/atoms';
import { useQueryClient } from '@tanstack/react-query';
import { updateUserInfo } from '@/apis/UserAPI';
import BasicModal from '@/components/molecules/BasicModal/BasicModal';
import InputForm from '../InputForm/InputForm';

interface ProfileModalProps {
    isOpen: boolean;
    title: string;
    namePlaceholder?: string;
    nicknamePlaceholder?: string;
    // 기존 데이터를 초기값으로 받을 수 있도록 추가
    initialName?: string;
    initialNickname?: string;
    initialProfileUrl?: string;
    onConfirm?: (data: {
        name: string;
        nickname: string;
        profilePicture?: File;
    }) => void;
    onCancel?: () => void;
}

const UserInfoUpdateModal = ({
    isOpen,
    title,
    namePlaceholder = '이름 입력',
    nicknamePlaceholder = '닉네임 입력',
    initialName = '',
    initialNickname = '',
    initialProfileUrl = '',
    onConfirm,
    onCancel,
}: ProfileModalProps) => {
    const [name, setName] = useState(initialName);
    const [nickname, setNickname] = useState(initialNickname);
    const [profilePicture, setProfilePicture] = useState<File | null>(null);
    // 파일 선택 전엔 기존 이미지 URL을 보여줌
    const [profilePreview, setProfilePreview] = useState<string | null>(
        initialProfileUrl || null,
    );
    const [isSuccessModalOpen, setIsSuccessModalOpen] =
        useState<boolean>(false);

    const queryClient = useQueryClient();

    // 모달이 열릴 때 기존 데이터로 입력값 설정, 닫힐 때는 초기화
    useEffect(() => {
        if (isOpen) {
            setName(initialName);
            setNickname(initialNickname);
            setProfilePicture(null);
            setProfilePreview(initialProfileUrl || null);
        } else {
            setName('');
            setNickname('');
            setProfilePicture(null);
            setProfilePreview(null);
        }
    }, [isOpen, initialName, initialNickname, initialProfileUrl]);

    // 프로필 사진 선택 시 미리보기 생성
    const handleProfileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files.length > 0) {
            const file = e.target.files[0];
            setProfilePicture(file);
            const reader = new FileReader();
            reader.onloadend = () => {
                setProfilePreview(reader.result as string);
            };
            reader.readAsDataURL(file);
        }
    };

    // 업로드한 프로필 사진 취소 (기존 이미지로 복원)
    const handleProfileCancel = () => {
        setProfilePicture(null);
        setProfilePreview(initialProfileUrl || null);
    };

    // 이름, 닉네임 유효성 검사: 필수, 영문/숫자/한글 1-30자
    const validateInputs = () => {
        const regex = /^[A-Za-z0-9가-힣]{1,30}$/;
        if (!name.trim()) {
            alert('이름 필드는 필수입니다.');
            return false;
        }
        if (!nickname.trim()) {
            alert('닉네임 필드는 필수입니다.');
            return false;
        }
        if (!regex.test(name)) {
            alert('이름은 영문, 숫자, 한글 1-30자만 가능합니다.');
            return false;
        }
        if (!regex.test(nickname)) {
            alert('닉네임은 영문, 숫자, 한글 1-30자만 가능합니다.');
            return false;
        }
        return true;
    };

    const handleConfirm = async () => {
        if (!validateInputs()) return;
        try {
            await updateUserInfo(nickname, name, profilePicture || undefined);
            await queryClient.invalidateQueries({ queryKey: ['myAllData'] });
            // 업데이트 성공 시 BasicModal을 오픈
            setIsSuccessModalOpen(true);
        } catch (error) {
            console.error('Error in updateUserInfo:', error);
        }
    };

    // BasicModal의 확인 버튼 클릭 시 처리: 모달 전체 닫기
    const handleSuccessModalConfirm = () => {
        setIsSuccessModalOpen(false);
        if (onConfirm) {
            onConfirm({
                name,
                nickname,
                profilePicture: profilePicture || undefined,
            });
        }
        if (onCancel) {
            onCancel();
        }
    };

    const handleCancel = () => {
        if (onCancel) {
            onCancel();
        }
    };

    return (
        <>
            <div
                className={`fixed inset-0 z-20 flex items-center justify-center bg-gray-600/20 ${
                    isOpen ? 'block' : 'hidden'
                }`}
            >
                <div className="w-full max-w-[352px] rounded-xl bg-gray-50 p-6 shadow-xl">
                    <section className="text-text-xl pb-1 text-center font-bold text-gray-900">
                        {title}
                    </section>

                    <section className="mb-4">
                        <InputForm
                            label="이름"
                            id="nameInput"
                            type="text"
                            placeholder={namePlaceholder}
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                        />
                    </section>

                    <section className="mb-4">
                        <InputForm
                            label="닉네임"
                            id="nicknameInput"
                            type="text"
                            placeholder={nicknamePlaceholder}
                            value={nickname}
                            onChange={(e) => setNickname(e.target.value)}
                        />
                    </section>

                    <section className="mb-4 flex flex-col items-center">
                        <label className="mb-2 block text-sm font-medium text-gray-700">
                            프로필 사진
                        </label>
                        {profilePreview ? (
                            <div className="flex flex-col items-center space-y-2">
                                <img
                                    src={profilePreview}
                                    alt="프로필 미리보기"
                                    className="h-24 w-24 rounded-full object-cover"
                                />
                                <ButtonPrimary
                                    action="cancel"
                                    onClick={handleProfileCancel}
                                    label="사진 취소"
                                />
                            </div>
                        ) : null}
                        {/* 파일 업로드 버튼 */}
                        <label className="mt-3 flex w-[120px] cursor-pointer items-center justify-center rounded-lg bg-blue-500 px-4 py-2 text-center text-white">
                            사진 업로드
                            <input
                                type="file"
                                accept="image/*"
                                className="hidden"
                                onChange={handleProfileChange}
                            />
                        </label>
                    </section>

                    <section className="flex justify-end space-x-2">
                        <ButtonPrimary
                            action="confirm"
                            onClick={handleConfirm}
                            label="변경"
                        />
                        <ButtonPrimary
                            action="cancel"
                            onClick={handleCancel}
                            label="취소"
                        />
                    </section>
                </div>
            </div>

            {/* 업데이트 성공 후 확인용 BasicModal */}
            <BasicModal
                isOpen={isSuccessModalOpen}
                title="닉네임 변경 성공!"
                onConfirmClick={handleSuccessModalConfirm}
            />
        </>
    );
};

export default UserInfoUpdateModal;
