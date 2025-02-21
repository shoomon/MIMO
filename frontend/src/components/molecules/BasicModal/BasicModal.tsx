import { ButtonPrimary, Icon } from '@/components/atoms';
import React from 'react';

interface BasicModalProps {
    title: string;
    isOpen: boolean;
    subTitle?: string;
    onConfirmClick?: (e: React.MouseEvent) => void;
    confirmLabel?: string;

    onDeleteClick?: (e: React.MouseEvent) => void;
    deleteLabel?: string;

    onCancelClick?: (e: React.MouseEvent) => void;
    cancelLabel?: string;
}

const BasicModal = ({
    isOpen,
    title,
    subTitle,
    onConfirmClick,
    confirmLabel = '확인',
    onDeleteClick,
    deleteLabel = '삭제',
    onCancelClick,
    cancelLabel = '취소',
}: BasicModalProps) => {
    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 z-20 flex items-center justify-center bg-gray-600/20">
            <div className="w-full max-w-[352px] rounded-xl bg-gray-50 p-6">
                {onDeleteClick && (
                    <div className="mb-2 flex w-full items-center justify-center">
                        <Icon id="Caution" type="svg" size={48} />
                    </div>
                )}
                {/* Title */}
                <section
                    className={`pb-${subTitle ? 2 : 8} text-text-xl text-center font-bold text-gray-900`}
                >
                    {title}
                </section>
                {/* Subtitle */}
                {subTitle && (
                    <section className="text-md pb-8 text-center font-medium text-gray-600">
                        {subTitle}
                    </section>
                )}
                {/* Buttons */}
                <section className="flex justify-center space-x-2">
                    {onConfirmClick && (
                        <ButtonPrimary
                            label={confirmLabel}
                            action="confirm"
                            onClick={onConfirmClick}
                        />
                    )}
                    {onDeleteClick && (
                        <ButtonPrimary
                            label={deleteLabel}
                            action="delete"
                            onClick={onDeleteClick}
                        />
                    )}
                    {onCancelClick && (
                        <ButtonPrimary
                            label={cancelLabel}
                            action="cancel"
                            onClick={onCancelClick}
                        />
                    )}
                </section>
            </div>
        </div>
    );
};

export default BasicModal;
