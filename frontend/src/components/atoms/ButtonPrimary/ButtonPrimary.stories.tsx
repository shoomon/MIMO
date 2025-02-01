import type { Meta, StoryObj } from '@storybook/react';
import ButtonPrimary from './ButtonPrimary';

const meta = {
    title: 'Components/Atoms/PrimaryButton/PrimaryButton',
    component: ButtonPrimary,
    parameters: {
        layout: 'centered',
    },
    tags: ['autodocs'],
} satisfies Meta<typeof ButtonPrimary>;

export default meta;
type Story = StoryObj<typeof meta>;

export const del: Story = {
    args: {
        action: 'delete',
        onClick: () => alert('delete'),
    },
    name: 'Delete',
};

export const confirm: Story = {
    args: {
        action: 'confirm',
        onClick: () => alert('confirm'),
    },
    name: 'Confirm',
};
export const cancel: Story = {
    args: {
        action: 'cancel',
        onClick: () => alert('cancel'),
    },
    name: 'Cancel',
};
