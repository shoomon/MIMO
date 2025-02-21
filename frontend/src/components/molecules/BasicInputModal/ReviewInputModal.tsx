// ReviewInputModal.tsx
import { useState, useEffect, useMemo } from 'react';
import { ButtonPrimary, Icon } from '@/components/atoms';
import InputForm from '../InputForm/InputForm';

interface ReviewInputModalProps {
    isOpen: boolean;
    title: string;
    subTitle?: string;
    inputPlaceholder?: string;
    confirmLabel: string;
    confirmDisabled: boolean;
    /**
     * onConfirmClick은 리뷰 텍스트와 별점 값을 객체로 전달합니다.
     */
    onConfirmClick?: (value: { reviewText: string; rating: number }) => void;
    onCancelClick?: () => void;
}

const ReviewInputModal = ({
    isOpen,
    title,
    subTitle,
    inputPlaceholder = '리뷰 내용을 입력하세요',
    confirmLabel,
    onConfirmClick,
    onCancelClick,
    confirmDisabled,
}: ReviewInputModalProps) => {
    // 리뷰 텍스트와 별점 상태 관리
    const [inputValue, setInputValue] = useState('');
    const [rating, setRating] = useState(0);
    const [error, setError] = useState<string | null>(null);

    // 모달이 닫힐 때 상태 초기화
    useEffect(() => {
        if (!isOpen) {
            setInputValue('');
            setRating(0);
            setError(null);
        }
    }, [isOpen]);

    const handleConfirm = () => {
        if (onConfirmClick && !error) {
            onConfirmClick({ reviewText: inputValue, rating });
        }
    };

    const handleCancel = () => {
        if (onCancelClick) {
            onCancelClick();
        }
    };

    // 입력값 변경 시 300자 초과 검사
    const handleInputChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    ) => {
        const value = e.target.value;
        if (value.length > 300) {
            setError('300자 초과입니다.');
        } else {
            setError(null);
        }
        setInputValue(value);
    };

    // 클릭만으로 설정하므로 hover 관련 코드는 제거하고 rating 상태만 사용
    const getStarIcon = (starIndex: number): string => {
        return rating >= starIndex ? 'Star' : 'emptyStar';
    };

    // 별점 영역만 메모이제이션 처리하여 입력할 때마다 별 영역이 다시 렌더링되지 않도록 함
    const starRating = useMemo(
        () => (
            <div className="flex justify-center gap-2">
                {[1, 2, 3, 4, 5].map((star) => (
                    <div
                        key={star}
                        className="relative h-10 w-10 cursor-pointer transition-opacity duration-300"
                        onClick={() => setRating(star)}
                    >
                        <div className="flex h-10 w-10 items-center justify-center">
                            <Icon
                                type="svg"
                                size={80}
                                id={getStarIcon(star)}
                                className="transition-opacity duration-300"
                            />
                        </div>
                    </div>
                ))}
            </div>
        ),
        [rating],
    );

    return (
        <div
            className={`fixed inset-0 z-20 flex items-center justify-center bg-gray-600/20 ${
                isOpen ? 'block' : 'hidden'
            }`}
        >
            <div className="w-full max-w-[552px] rounded-xl bg-gray-50 p-6 shadow-xl">
                {/* 제목 */}
                <section className="text-text-xl pb-1 text-center font-bold text-gray-900">
                    {title}
                </section>
                {subTitle && (
                    <section className="pb-4 text-center text-sm font-medium text-gray-600">
                        {subTitle}
                    </section>
                )}
                {/* 별점 입력 영역 */}
                <section className="mb-4 flex flex-col items-center gap-2">
                    {starRating}
                    {/* 선택된 별점 숫자 표시 */}
                    <div className="text-lg font-bold text-gray-800">
                        {rating} / 5
                    </div>
                </section>
                {/* 리뷰 입력 */}
                <section className="mb-4">
                    <InputForm
                        id="reviewInput"
                        type="text"
                        placeholder={inputPlaceholder}
                        value={inputValue}
                        onChange={handleInputChange}
                        multiline
                        count={300}
                    />
                </section>
                {/* 확인/취소 버튼 */}
                <section className="flex justify-center space-x-2">
                    <ButtonPrimary
                        action="confirm"
                        onClick={handleConfirm}
                        label={confirmLabel}
                        disabled={!!error || confirmDisabled}
                    />
                    <ButtonPrimary
                        action="cancel"
                        onClick={handleCancel}
                        label="취소"
                    />
                </section>
            </div>
        </div>
    );
};

export default ReviewInputModal;
