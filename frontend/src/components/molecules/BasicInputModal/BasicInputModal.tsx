import { useState, useEffect } from 'react';
import { ButtonPrimary, Input } from '@/components/atoms';

interface BasicInputModalProps {
    isOpen: boolean;
    title: string;
    subTitle?: string;
    inputPlaceholder?: string;
    confirmLabel: string;
    onConfirmClick?: (value: string) => void;
    onCancelClick?: () => void;
}

const BasicInputModal = ({
    isOpen,
    title,
    subTitle,
    inputPlaceholder = '입력',
    confirmLabel,
    onConfirmClick,
    onCancelClick,
}: BasicInputModalProps) => {
    // 모달 내부에서 입력값을 관리합니다.
    const [inputValue, setInputValue] = useState('');

    // 모달이 닫힐 때 입력값 초기화
    useEffect(() => {
        if (!isOpen) {
            setInputValue('');
        }
    }, [isOpen]);

    const handleConfirm = () => {
        if (onConfirmClick) {
            onConfirmClick(inputValue);
        }
    };

    const handleCancel = () => {
        if (onCancelClick) {
            onCancelClick();
        }
    };

    return (
        <div
            className={`fixed inset-0 z-20 flex items-center justify-center bg-gray-600/20 ${
                isOpen ? 'block' : 'hidden'
            }`}
        >
            <div className="w-full max-w-[352px] rounded-xl bg-gray-50 p-6 shadow-xl">
                <section className="text-text-xl pb-4 text-center font-bold text-gray-900">
                    {title}
                </section>
                {subTitle && (
                    <section className="pb-4 text-center text-sm font-medium text-gray-600">
                        {subTitle}
                    </section>
                )}
                <section className="mb-4">
                    <Input
                        id="boardNameInput"
                        type="text"
                        placeholder={inputPlaceholder}
                        value={inputValue}
                        onChange={(e) => setInputValue(e.target.value)}
                    />
                </section>
                <section className="flex justify-end space-x-2">
                    <ButtonPrimary
                        action="confirm"
                        onClick={handleConfirm}
                        label={confirmLabel}
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

export default BasicInputModal;
