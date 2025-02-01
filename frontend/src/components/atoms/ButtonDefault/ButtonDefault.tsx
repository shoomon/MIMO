// ButtonDefault.tsx
import React from 'react';
import { ButtonDefaultView } from './ButtonDefaultView';

interface ButtonDefaultProps {
    type?: 'default' | 'primary' | 'fail';
    htmlType?: 'button' | 'submit' | 'reset';
    onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
    disabled?: boolean;
    iconId?: string;
    iconType?: 'svg' | 'png';
    content: string;
}

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
