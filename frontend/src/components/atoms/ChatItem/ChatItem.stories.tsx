import type { Meta, StoryObj } from '@storybook/react';
import ChatItem from './ChatItem';

const meta = {
    title: 'Components/Atoms/ChatItem',
    component: ChatItem,
    parameters: {
        layout: 'centered',
    },
    tags: ['autodocs'],
} satisfies Meta<typeof ChatItem>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Sender: Story = {
    args: {
        type: 'sender',
        message: 'Message content goes here',
    },
};

export const Receiver: Story = {
    args: {
        type: 'receiver',
        message: 'Message content goes here',
    },
};
