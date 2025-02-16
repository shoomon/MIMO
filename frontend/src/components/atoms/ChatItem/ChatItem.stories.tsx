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

export const Default: Story = {
    args: {
        type: 'receiver',
        item: {
            id: '12',
            nickname: '박성문',
            profileImageUri:
                'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            chat: '안녕하세요 박성문입니다.',
            timestamp: '2025-02-11T19:00:45.7557546',
            chatType: 'MESSAGE',
        },
        hasReceivedMessage: false,
    },
};

export const Receive: Story = {
    args: {
        type: 'receiver',
        item: {
            id: '12',
            nickname: '박성문',
            profileImageUri:
                'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            chat: '안녕하세요 박성문입니다.',
            timestamp: '2025-02-11T19:00:45.7557546',
            chatType: 'MESSAGE',
        },
        hasReceivedMessage: false,
    },
};

export const Sender: Story = {
    args: {
        type: 'sender',
        item: {
            id: '12',
            nickname: '박성문',
            profileImageUri:
                "'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg",
            chat: '안녕하세요 박성문입니다.',
            timestamp: '2025-02-11T19:00:45.7557546',
            chatType: 'MESSAGE',
        },
        hasReceivedMessage: true,
    },
};

export const Receiver: Story = {
    args: {
        type: 'receiver',
        item: {
            id: '12',
            nickname: '박성문',
            profileImageUri:
                'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            chat: '안녕하세요 박성문입니다.',
            timestamp: '2025-02-11T19:00:45.7557546',
            chatType: 'MESSAGE',
        },
        hasReceivedMessage: false,
    },
};
