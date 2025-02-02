import ButtonToggle from '../../atoms/ButtonToggle/ButtonToggle';
interface ButtonToggleGroupViewProps {
    options: { value: string; label: string }[];
    activeValue: string;
    onButtonClick: (value: string) => void;
}

const ButtonToggleGroupView = ({
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

export default ButtonToggleGroupView;
