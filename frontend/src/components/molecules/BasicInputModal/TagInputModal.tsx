import { useState, useEffect } from 'react';
import { ButtonPrimary, Input } from '@/components/atoms';

interface TagInputModalProps {
    isOpen: boolean;
    title: string;
    subTitle?: string;
    inputPlaceholder?: string;
    confirmLabel: string;

    /**
     * 카테고리/지역 태그 목록과 부모에서 입력 중인 `pendingTags`를 함께 받아
     * 모달에서 즉시 중복 검사 가능
     */
    existingCategoryTags: string[];
    existingAreaTags: string[];
    existingPendingTags: string[];

    /** 모달에서 최종 확인 시 전달할 콜백 (새 태그 배열) */
    onConfirmClick?: (tags: string[]) => void;
    onCancelClick?: () => void;
}

const TagInputModal = ({
    isOpen,
    title,
    subTitle,
    inputPlaceholder = '새 태그 입력',
    confirmLabel,
    existingCategoryTags,
    existingAreaTags,
    existingPendingTags,
    onConfirmClick,
    onCancelClick,
}: TagInputModalProps) => {
    const [inputValue, setInputValue] = useState('');
    const [pendingTags, setPendingTags] = useState<string[]>([]);
    const [errorMessage, setErrorMessage] = useState('');

    // 모달 열릴 때마다 상태 초기화
    useEffect(() => {
        if (!isOpen) {
            setInputValue('');
            setPendingTags([]);
            setErrorMessage('');
        }
    }, [isOpen]);

    // 간단한 유효성 검사 (영문, 숫자, 한글, 길이 1~30자)
    const validateTag = (tag: string) => {
        const regex = /^[A-Za-z0-9가-힣]{1,30}$/;
        return regex.test(tag);
    };

    // "추가" 버튼 클릭 시: 입력값 바로 검사/중복체크, 문제 없으면 모달 내부 pendingTags에 추가
    const handleAddTag = () => {
        const newTag = inputValue.trim();
        if (!newTag) return;

        // 1) 길이/문자 검사
        if (!validateTag(newTag)) {
            setErrorMessage(
                `'${newTag}' 태그는 1~30자 영문/숫자/한글만 가능합니다.`,
            );
            return;
        }
        // 2) 중복 검사
        if (
            pendingTags.includes(newTag) ||
            existingPendingTags.includes(newTag) ||
            existingCategoryTags.includes(newTag) ||
            existingAreaTags.includes(newTag)
        ) {
            setErrorMessage(`'${newTag}'은(는) 이미 존재하는 태그입니다.`);
            return;
        }

        // 모두 통과 시 추가
        setErrorMessage('');
        setPendingTags([...pendingTags, newTag]);
        setInputValue('');
    };

    // 모달 내 임시 태그 목록에서 제거
    const removeTag = (tag: string) => {
        setPendingTags(pendingTags.filter((t) => t !== tag));
    };

    // "확인" 버튼 클릭 시 -> 부모에게 최종 pendingTags 넘김
    const handleConfirm = () => {
        if (onConfirmClick) {
            onConfirmClick(pendingTags);
        }
    };

    return (
        <div
            className={`fixed inset-0 z-30 flex items-center justify-center bg-gray-600/20 ${
                isOpen ? 'block' : 'hidden'
            }`}
        >
            <div className="w-full max-w-md rounded-xl bg-gray-50 p-6 shadow-xl">
                <h2 className="text-center text-xl font-bold text-gray-900">
                    {title}
                </h2>
                {subTitle && (
                    <p className="text-center text-sm font-medium text-gray-600">
                        {subTitle}
                    </p>
                )}

                {/* 태그 입력 영역 */}
                <div className="mt-4 flex items-center gap-2">
                    <Input
                        id="tagInput"
                        type="text"
                        placeholder={inputPlaceholder}
                        value={inputValue}
                        onChange={(e) => {
                            setInputValue(e.target.value);
                            setErrorMessage('');
                        }}
                    />
                    <ButtonPrimary
                        action="confirm"
                        onClick={handleAddTag}
                        label="추가"
                    />
                </div>
                {errorMessage && (
                    <div className="mt-2 text-sm text-red-500">
                        {errorMessage}
                    </div>
                )}

                {/* 모달 내부에서 추가된 태그 목록 */}
                {pendingTags.length > 0 && (
                    <div className="mt-4">
                        <p className="font-semibold">
                            추가할 태그 (모달 내 임시):
                        </p>
                        <div className="mt-1 flex flex-wrap gap-2">
                            {pendingTags.map((tag, idx) => (
                                <div
                                    key={idx}
                                    className="flex items-center gap-1 rounded bg-gray-200 px-2 py-1"
                                >
                                    <span>{tag}</span>
                                    <button
                                        type="button"
                                        onClick={() => removeTag(tag)}
                                        className="text-red-500"
                                    >
                                        X
                                    </button>
                                </div>
                            ))}
                        </div>
                    </div>
                )}

                {/* 확인/취소 버튼 */}
                <div className="mt-4 flex justify-end space-x-2">
                    <ButtonPrimary
                        action="confirm"
                        onClick={handleConfirm}
                        label={confirmLabel}
                    />
                    <ButtonPrimary
                        action="cancel"
                        onClick={onCancelClick}
                        label="취소"
                    />
                </div>
            </div>
        </div>
    );
};

export default TagInputModal;
