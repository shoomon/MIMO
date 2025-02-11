import Icon from '../Icon/Icon';

interface ButtonDefaultViewProps {
    htmlType: 'button' | 'submit' | 'reset';
    onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
    disabled: boolean;
    buttonClass: string;
    content: string;
    iconId?: string;
    iconType?: 'svg' | 'png';
}

const ButtonDefaultView = ({
    htmlType,
    onClick,
    disabled,
    buttonClass,
    content,
    iconId,
    iconType,
}: ButtonDefaultViewProps) => {
    return (
        <button
            type={htmlType}
            onClick={onClick}
            disabled={disabled}
            className={`hover:bg-brand-primary-100 flex h-[35px] w-fit cursor-pointer items-center justify-center gap-2 rounded-sm px-[8px] py-[4px] font-semibold ${buttonClass}`}
        >
            {iconId && <Icon id={iconId} type={iconType || 'svg'} />}
            <span className="whitespace-nowrap">{content}</span>
        </button>
    );
};

export default ButtonDefaultView;
