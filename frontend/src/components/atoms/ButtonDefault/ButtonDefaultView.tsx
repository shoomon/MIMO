import React from 'react';
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

export const ButtonDefaultView = ({
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
            className={`flex h-[35px] w-fit items-center justify-center gap-2 rounded-md px-[8px] py-[4px] font-bold font-semibold ${buttonClass}`}
        >
            {iconId && <Icon id={iconId} type={iconType || 'svg'} />}
            <span>{content}</span>
        </button>
    );
};
