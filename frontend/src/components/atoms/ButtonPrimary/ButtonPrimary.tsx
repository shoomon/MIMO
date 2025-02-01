// PrimaryButton.tsx
import { ButtonPrimaryView } from './ButtonPrimaryView';

interface ButtonPrimaryProps {
    htmlType?: 'button' | 'submit' | 'reset';
    action: 'delete' | 'confirm' | 'cancel';
    onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
    disabled?: boolean;
}

export const ButtonPrimary = ({
    htmlType = 'button',
    action,
    onClick,
    disabled = false,
}: ButtonPrimaryProps) => {
    const content =
        action === 'delete' ? '삭제' : action === 'confirm' ? '확인' : '취소';

    const BUTTON_STYLES: Record<typeof action, string> = {
        delete: 'bg-fail text-white',
        confirm: 'bg-brand-primary-400 text-white',
        cancel: 'bg-white text-dark border border-gray-300',
    };

    const baseStyle = BUTTON_STYLES[action];

    const disabledStyle =
        disabled && (action === 'delete' || action === 'confirm')
            ? 'opacity-70'
            : '';

    const className = `${baseStyle} ${disabledStyle}`;

    return (
        <ButtonPrimaryView
            htmlType={htmlType}
            onClick={onClick}
            disabled={disabled}
            className={className}
        >
            {content}
        </ButtonPrimaryView>
    );
};

export default ButtonPrimary;
