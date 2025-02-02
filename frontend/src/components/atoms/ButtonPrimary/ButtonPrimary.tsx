import ButtonPrimaryView from './ButtonPrimary.view';

interface ButtonPrimaryProps {
    htmlType?: 'button' | 'submit' | 'reset';
    action: 'delete' | 'confirm' | 'cancel';
    onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
    disabled?: boolean;
}

/**
 * ButtonPrimary 컴포넌트는 삭제, 확인, 취소와 같은 액션에 따른 스타일과 텍스트를 가진 기본 버튼을 렌더링합니다.
 *
 * @component
 * @param {('button' | 'submit' | 'reset')} [htmlType='button'] - HTML 버튼의 타입입니다.
 * @param {('delete' | 'confirm' | 'cancel')} action - 버튼의 액션 유형입니다.
 * @param {(e: React.MouseEvent<HTMLButtonElement>) => void} [onClick] - 클릭 이벤트 핸들러입니다.
 * @param {boolean} [disabled=false] - 버튼의 비활성화 여부입니다.
 * @returns {JSX.Element} - 렌더링된 ButtonPrimary 컴포넌트를 반환합니다.
 */
const ButtonPrimary = ({
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
