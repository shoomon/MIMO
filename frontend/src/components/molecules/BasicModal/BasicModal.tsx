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
            <div className="w-full max-w-[352px] rounded-xl border bg-gray-50 p-6">
                <section
                    className={`pb-${subTitle ? 2 : 8} text-text-xl text-center font-bold text-gray-900`}
                >
                    {title}
                </section>
                {subTitle && (
                    <section className="pb-8 text-center text-sm font-medium text-gray-600">
                        {subTitle}
                    </section>
                )}
                <section className="flex space-x-2">
                    {onConfirmClick && (
                        <ButtonPrimary
                            action="confirm"
                            onClick={onConfirmClick}
                        />
                    )}

                    {onCancelClick && (
                        <ButtonPrimary
                            action="cancel"
                            onClick={onCancelClick}
                        />
                    )}
                    {onDeleteClick && (
                        <ButtonPrimary
                            action="delete"
                            onClick={onDeleteClick}
                        />
                    )}
                </section>
            </div>
        </div>
    );
};

export default BasicModal;
