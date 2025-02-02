import ButtonDefaultView from './ButtonDefault.view';

interface ButtonDefaultProps {
    type?: 'default' | 'primary' | 'fail';
    htmlType?: 'button' | 'submit' | 'reset';
    onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
    disabled?: boolean;
    iconId?: string;
    iconType?: 'svg' | 'png';
    content: string;
}

/**
 * ButtonDefault 컴포넌트는 다양한 스타일 타입과 아이콘 옵션을 가진 버튼을 렌더링합니다.
 *
 * @component
 * @param {('default' | 'primary' | 'fail')} [type='default'] - 버튼의 스타일 타입입니다.
 * @param {('button' | 'submit' | 'reset')} [htmlType='button'] - HTML 버튼의 동작 타입입니다.
 * @param {(e: React.MouseEvent<HTMLButtonElement>) => void} [onClick] - 클릭 이벤트 핸들러입니다.
 * @param {boolean} [disabled=false] - 버튼의 비활성화 여부를 결정합니다.
 * @param {string} [iconId] - 아이콘의 고유 식별자입니다. 값이 있을 경우 아이콘이 렌더링됩니다.
 * @param {('svg' | 'png')} [iconType='svg'] - 렌더링할 아이콘의 타입을 결정합니다.
 * @param {string} content - 버튼에 표시할 텍스트입니다.
 * @returns {JSX.Element} - 렌더링된 ButtonDefault 컴포넌트를 반환합니다.
 */
const ButtonDefault = ({
    type = 'default',
    htmlType = 'button',
    onClick,
    disabled = false,
    iconId,
    iconType = 'svg',
    content,
}: ButtonDefaultProps) => {
    // type 값에 따라 버튼 스타일(컬러) 결정
    let buttonClass = '';
    switch (type) {
        case 'primary':
            buttonClass = 'bg-brand-primary-400 text-white';
            break;
        case 'fail':
            buttonClass = 'bg-fail text-white';
            break;
        case 'default':
        default:
            buttonClass = 'bg-white text-black border border-black';
            break;
    }

    // disabled 상태면 opacity 효과 적용
    if (disabled) {
        buttonClass += ' opacity-70';
    }

    return (
        <ButtonDefaultView
            htmlType={htmlType}
            onClick={onClick}
            disabled={disabled}
            buttonClass={buttonClass}
            content={content}
            iconId={iconId}
            iconType={iconType}
        />
    );
};

export default ButtonDefault;
