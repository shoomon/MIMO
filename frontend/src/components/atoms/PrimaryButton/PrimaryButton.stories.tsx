import type { Meta, StoryObj } from '@storybook/react';
import PrimaryButton from './PrimaryButton';

const meta = {
    title: 'Components/Atoms/PrimaryButton/PrimaryButton',
    component: PrimaryButton,
    parameters: {
        layout: 'centered',
    },
    tags: ['autodocs'],
} satisfies Meta<typeof PrimaryButton>;

export default meta;
type Story = StoryObj<typeof meta>;

export const del: Story = {
    args: {
        action: 'delete',
        onClick: () => alert('delete'),
    },
};

export const confirm: Story = {
    args: {
        action: 'confirm',
        onClick: () => alert('confirm'),
    },
};
export const cancel: Story = {
    args: {
        action: 'cancel',
        onClick: () => alert('cancel'),
    },
};
