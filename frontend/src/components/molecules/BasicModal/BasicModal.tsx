import { ButtonPrimary } from '@/components/atoms';

interface BasicModalProps {
    title: string;
    isOpen: boolean;
    subTitle?: string;
    onConfirmClick?: (e: React.MouseEvent) => void;
    onDeleteClick?: (e: React.MouseEvent) => void;
    onCancelClick?: (e: React.MouseEvent) => void;
}

const BasicModal = ({
    isOpen,
    title,
    subTitle,
    onConfirmClick,
    onDeleteClick,
    onCancelClick,
}: BasicModalProps) => {
    return (
        <div
            className={`fixed inset-0 z-20 flex items-center justify-center bg-gray-600/20 ${
                isOpen ? 'block' : 'hidden'
            }`}
        >
            <div className="w-full max-w-[352px] rounded-xl bg-gray-50 p-6">
                <section
                    className={`pb-${subTitle ? 2 : 8} text-text-xl text-center font-bold text-gray-900`}
                >
                    {title}
                </section>
                {subTitle && (
                    <section className="text-md pb-8 text-center font-medium text-gray-600">
                        {subTitle}
                    </section>
                )}
                <section className="flex space-x-2">
                    {onConfirmClick && (
                        <ButtonPrimary
                            label="확인"
                            action="confirm"
                            onClick={onConfirmClick}
                        />
                    )}
                    {onDeleteClick && (
                        <ButtonPrimary
                            label="확인"
                            action="delete"
                            onClick={onDeleteClick}
                        />
                    )}
                    {onCancelClick && (
                        <ButtonPrimary
                            label="취소"
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
