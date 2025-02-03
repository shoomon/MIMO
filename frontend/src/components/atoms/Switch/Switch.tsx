import { useState } from 'react';

// ----------------------------------------------------------------------
// View Component: 오직 UI 렌더링만 담당
// ----------------------------------------------------------------------

interface SwitchViewProps {
    /**
     * 버튼의 비활성화 여부를 나타냅니다.
     */
    disabled: boolean;

    /**
     * 스위치의 활성화 상태를 나타냅니다.
     */
    isOn: boolean;

    /**
     * 버튼 클릭 시 실행되는 함수입니다.
     *
     * @param {React.MouseEvent<HTMLButtonElement>} e - 클릭 이벤트 객체
     */
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

/**
 * `SwitchView` 컴포넌트는 스위치 UI를 렌더링합니다.
 * 상태 관리 없이 UI만 담당합니다.
 *
 * @component
 * @param {boolean} disabled - 버튼의 비활성화 여부입니다.
 * @param {boolean} isOn - 현재 스위치의 상태입니다.
 * @param {(e: React.MouseEvent<HTMLButtonElement>) => void} onClick - 클릭 이벤트 핸들러입니다.
 * @returns {JSX.Element} - 렌더링된 `SwitchView` 컴포넌트를 반환합니다.
 */
const SwitchView = ({
    disabled,
    isOn,
    onClick,
}: SwitchViewProps): JSX.Element => (
    <button
        onClick={onClick}
        disabled={disabled}
        className={`relative flex h-[20px] w-[40px] items-center rounded-full px-[2px] transition-all duration-300 ease-in-out ${
            isOn
                ? 'bg-brand-primary-400 border-brand-primary-400 hover:bg-brand-primary-500 hover:border-brand-primary-500 border'
                : 'border border-gray-700 bg-white hover:bg-gray-200'
        }`}
    >
        <div
            className={`absolute h-[14px] w-[14px] rounded-full transition-transform duration-300 ease-in-out ${
                isOn
                    ? 'translate-x-[20px] bg-white'
                    : 'translate-x-0 bg-gray-600'
            }`}
        ></div>
    </button>
);

// ----------------------------------------------------------------------
// Container Component: 상태 관리 및 이벤트 핸들링 담당
// ----------------------------------------------------------------------

interface SwitchContainerProps {
    /**
     * 버튼의 비활성화 여부를 나타냅니다. (기본값: `false`)
     */
    disabled?: boolean;

    /**
     * 초기 활성화 상태를 나타냅니다.
     */
    isactive: boolean;

    /**
     * 버튼 클릭 시 실행되는 함수입니다.
     *
     * @param {React.MouseEvent<HTMLButtonElement>} e - 클릭 이벤트 객체
     */
    onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

/**
 * `SwitchContainer` 컴포넌트는 상태를 관리하고 `SwitchView`를 렌더링하는 컨테이너 역할을 합니다.
 *
 * @component
 * @param {boolean} [disabled=false] - 버튼의 비활성화 여부입니다.
 * @param {boolean} isactive - 초기 활성화 상태입니다.
 * @param {(e: React.MouseEvent<HTMLButtonElement>) => void} [onClick] - 클릭 이벤트 핸들러입니다.
 * @returns {JSX.Element} - 렌더링된 `SwitchContainer` 컴포넌트를 반환합니다.
 */
const SwitchContainer = ({
    disabled = false,
    isactive,
    onClick,
}: SwitchContainerProps): JSX.Element => {
    const [isOn, setIsOn] = useState<boolean>(isactive);

    /**
     * 클릭 이벤트 핸들러로, 스위치의 상태를 변경합니다.
     *
     * @param {React.MouseEvent<HTMLButtonElement>} e - 클릭 이벤트 객체
     */
    const handleClick = (e: React.MouseEvent<HTMLButtonElement>) => {
        if (disabled) return;
        setIsOn(!isOn);
        if (onClick) {
            onClick(e);
        }
    };

    return <SwitchView disabled={disabled} isOn={isOn} onClick={handleClick} />;
};

export default SwitchContainer;
