import ButtonToggleView from './ButtonToggle.view';

interface ButtonToggleProps {
    /** 버튼의 고유 값 */
    value: string;
    /** 현재 버튼의 활성 상태 */
    active: boolean;
    /** 버튼 클릭 시 호출, value를 전달 */
    onClick: (value: string) => void;
    /** 버튼에 표시할 라벨 */
    children: React.ReactNode;
}

/**
 * ToggleButton 컴포넌트는 단일 토글 버튼으로, 클릭 시 자신의 value를 상위에 전달합니다.
 *
 * @component
 * @param {string} value - 버튼의 고유 값.
 * @param {boolean} active - 현재 버튼의 활성 상태.
 * @param {(value: string) => void} onClick - 버튼 클릭 시 호출되는 콜백 함수로, 자신의 value를 인자로 전달합니다.
 * @param {React.ReactNode} children - 버튼에 표시할 라벨 또는 내용을 나타냅니다.
 * @returns {JSX.Element} - 렌더링된 ToggleButton 컴포넌트를 반환합니다.
 */
const ButtonToggle = ({
    value,
    active,
    onClick,
    children,
}: ButtonToggleProps) => {
    const handleClick = () => onClick(value);
    return (
        <ButtonToggleView value={value} active={active} onClick={handleClick}>
            {children}
        </ButtonToggleView>
    );
};

export default ButtonToggle;
