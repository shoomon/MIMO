/**
 * PrimaryButton 컴포넌트는 사용자 동작을 유도하는 기본 버튼입니다.
 * 다양한 액션(`delete`, `confirm`, `cancel`)을 지원하며, `disabled` 상태에서 `opacity` 효과가 적용됩니다.
 *
 * @component
 * @example
 * ```tsx
 * <PrimaryButton action="confirm" onClick={() => alert("확인 버튼 클릭")} />
 * ```
 *
 * @prop { "button" | "submit" | "reset" } [type="button"] - 버튼의 HTML 타입
 * @prop { "delete" | "confirm" | "cancel" } action - 버튼의 동작 유형 (필수)
 * @prop { (e: React.MouseEvent<HTMLButtonElement>) => void } [onClick] - 클릭 이벤트 핸들러
 * @prop { boolean } [disabled=false] - 버튼 비활성화 여부
 */

interface PrimaryButtonProps {
    type?: 'button' | 'submit' | 'reset';
    action: 'delete' | 'confirm' | 'cancel';
    onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
    disabled?: boolean;
    // loading?: boolean;
}

const PrimaryButton = ({
    type = 'button',
    action,
    onClick,
    disabled = false,
}: PrimaryButtonProps) => {
    const content =
        action === 'delete' ? '삭제' : action === 'confirm' ? '확인' : '취소';

    const BUTTON_STYLES = {
        delete: 'bg-fail text-white',
        confirm: 'bg-brand-primary-400 text-white',
        cancel: 'bg-white text-dark border border-gray-300',
    };
    return (
        <button
            disabled={disabled}
            type={type}
            onClick={onClick}
            className={`flex h-[44px] w-[170px] justify-center gap-2 rounded-lg px-[18px] py-[10px] font-bold ${BUTTON_STYLES[action]} ${disabled && (action === 'delete' || action === 'confirm') ? 'opacity-70' : ''}`}
        >
            {content}
        </button>
    );
};

export default PrimaryButton;
