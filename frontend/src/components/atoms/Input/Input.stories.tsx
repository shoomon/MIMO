import type { Meta, StoryObj } from '@storybook/react';
import Input from './Input';

const meta: Meta<typeof Input> = {
    title: 'Components/Atoms/Input',
    component: Input,
    tags: ['autodocs'],
    argTypes: {
        type: {
            control: 'select',
            options: ['text', 'email', 'number', 'password'],
        },
        value: { control: 'text' },
        defaultValue: { control: 'text' },
        placeholder: { control: 'text' },
        disabled: { control: 'boolean' },
        readOnly: { control: 'boolean' },
        multiline: { control: 'boolean' },
        rows: { control: 'number' },
        onChange: { action: 'changed' },
    },
};

export default meta;
type Story = StoryObj<typeof Input>;

export const Default: Story = {
    args: {
        id: 'default-input',
        placeholder: '텍스트 입력...',
        type: 'text',
        multiline: false,
    },
};

export const Disabled: Story = {
    args: {
        id: 'disabled-input',
        placeholder: '비활성화됨',
        disabled: true,
        multiline: false,
    },
};

export const ReadOnly: Story = {
    args: {
        id: 'readonly-input',
        value: '읽기 전용 텍스트',
        readOnly: true,
        multiline: false,
    },
};

export const WithDefaultValue: Story = {
    args: {
        id: 'default-value-input',
        defaultValue: '기본 값',
        multiline: false,
    },
};

export const NumberInput: Story = {
    args: {
        id: 'number-input',
        type: 'number',
        placeholder: '숫자를 입력하세요...',
        multiline: false,
    },
};

export const PasswordInput: Story = {
    args: {
        id: 'password-input',
        type: 'password',
        placeholder: '비밀번호 입력...',
        multiline: false,
    },
};

// VAC 패턴에 따라 동일한 Input 컴포넌트로 multiline 지원
export const Multiline: Story = {
    args: {
        id: 'multiline-input',
        placeholder: '여러 줄 입력...',
        multiline: true,
        rows: 6,
    },
};
