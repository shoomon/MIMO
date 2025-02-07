// InputForm.stories.tsx
import type { Meta, StoryObj } from '@storybook/react';
import InputForm from './InputForm';

const meta: Meta<typeof InputForm> = {
    title: 'Components/Molecules/InputForm',
    component: InputForm,
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
        onChange: { action: 'changed' },
        onKeyDown: { action: 'keyDown' },
        count: { control: 'number' },
        errorMessage: { control: 'text' },
    },
};

export default meta;
type Story = StoryObj<typeof InputForm>;

export const Default: Story = {
    args: {
        id: 'default-input-form',
        label: 'Name',
        placeholder: 'Enter your name...',
        type: 'text',
    },
};

export const WithDefaultValue: Story = {
    args: {
        id: 'default-value-input-form',
        label: 'Email',
        defaultValue: 'user@example.com',
        placeholder: 'Enter your email...',
        type: 'email',
    },
};

export const ReadOnly: Story = {
    args: {
        id: 'readonly-input-form',
        label: 'Username',
        value: 'readonly_username',
        readOnly: true,
    },
};

export const ErrorState: Story = {
    args: {
        id: 'error-input-form',
        label: 'Password',
        placeholder: 'Enter your password...',
        type: 'password',
        errorMessage: 'Password is too short',
    },
};

export const WithCount: Story = {
    args: {
        id: 'count-input-form',
        label: 'Comment',
        placeholder: 'Write your comment...',
        count: 100,
    },
};
