import { ButtonToggle } from '../../atoms/ButtonToggle/ButtonToggle';
export interface ButtonToggleGroupViewProps {
    options: { value: string; label: string }[];
    activeValue: string;
    onButtonClick: (value: string) => void;
}

export const ButtonToggleGroupView = ({
    options,
    activeValue,
    onButtonClick,
}: ButtonToggleGroupViewProps) => {
    return (
        <div className="flex gap-2">
            {options.map((option) => (
                <ButtonToggle
                    key={option.value}
                    value={option.value}
                    active={activeValue === option.value}
                    onClick={onButtonClick}
                >
                    {option.label}
                </ButtonToggle>
            ))}
        </div>
    );
};
