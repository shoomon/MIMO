import { useState } from 'react';

// ----------------------------------------------------------------------
// View Component: 오직 UI 렌더링만 담당
// ----------------------------------------------------------------------
interface SwitchViewProps {
    disabled: boolean;
    isOn: boolean;
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const SwitchView = ({ disabled, isOn, onClick }: SwitchViewProps) => (
    <button
        onClick={onClick}
        disabled={disabled}
        className={`relative flex h-[20px] w-[40px] items-center rounded-full px-[2px] transition-all duration-300 ease-in-out ${
            isOn
                ? 'bg-brand-primary-400 border-brand-primary-400 border'
                : 'border border-gray-700 bg-white'
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
    disabled?: boolean;
    isactive: boolean;
    onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const SwitchContainer = ({
    disabled = false,
    isactive,
    onClick,
}: SwitchContainerProps) => {
    const [isOn, setIsOn] = useState<boolean>(isactive);

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
