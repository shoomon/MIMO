// PrimaryButtonView.tsx
import React from 'react';

interface ButtonPrimaryViewProps {
    htmlType: 'button' | 'submit' | 'reset';
    onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
    disabled: boolean;
    className: string;
    children: React.ReactNode;
}

export const ButtonPrimaryView = ({
    htmlType,
    onClick,
    disabled,
    className,
    children,
}: ButtonPrimaryViewProps) => (
    <button
        htmlType={htmlType}
        onClick={onClick}
        disabled={disabled}
        className={`flex h-[44px] w-[170px] justify-center rounded-lg px-[18px] py-[10px] font-bold ${className}`}
    >
        {children}
    </button>
);
