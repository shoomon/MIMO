import { useState } from 'react';
import ButtonToggleGroupView from './ButtonToggleGroup.view';

interface ButtonToggleGroupProps {
    /** 버튼 옵션 배열 */
    options: { value: string; label: string }[];
    /** 초기 선택될 버튼의 value (optional) */
    defaultValue?: string;
    /** 선택이 변경될 때 호출되는 콜백 */
    onChange?: (value: string) => void;
}

/**
 * ButtonToggleGroup 컴포넌트는 여러 토글 버튼을 그룹으로 관리하며,
 * 하나의 버튼이 선택되면 나머지 버튼은 자동으로 비활성화됩니다.
 *
 * @component
 * @param {Array<{ value: string; label: string }>} options - 버튼 옵션 배열. 각 옵션은 고유 value와 label을 포함합니다.
 * @param {string} [defaultValue] - 초기 선택될 버튼의 value (선택 사항).
 * @param {(value: string) => void} [onChange] - 선택이 변경될 때 호출되는 콜백 함수로, 변경된 버튼의 value를 인자로 전달합니다.
 * @returns {JSX.Element} - 렌더링된 ButtonToggleGroup 컴포넌트를 반환합니다.
 */
const ButtonToggleGroup = ({
    options,
    defaultValue,
    onChange,
}: ButtonToggleGroupProps) => {
    const [activeValue, setActiveValue] = useState<string>(defaultValue || '');

    const handleButtonClick = (value: string) => {
        setActiveValue(value);
        if (onChange) {
            onChange(value);
        }
    };

    return (
        <ButtonToggleGroupView
            options={options}
            activeValue={activeValue}
            onButtonClick={handleButtonClick}
        />
    );
};

export default ButtonToggleGroup;
