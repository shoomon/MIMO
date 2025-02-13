import type { Meta, StoryObj } from '@storybook/react';
import { BrowserRouter } from 'react-router-dom';
import ChatRoomDetail from './ChatRoomDetail';

const meta = {
    title: 'Components/Organisms/ChatRoomDetail',
    component: ChatRoomDetail,
    parameters: {
        layout: 'fullscreen',
    },
    decorators: [
        (Story) => (
            <BrowserRouter>
                <Story />
            </BrowserRouter>
        ),
    ],
} satisfies Meta<typeof ChatRoomDetail>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        chatroomName: '박성문 회담',
        chatroomImage:
            'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
        chatData: [
            {
                type: 'sender',
                item: {
                    id: '12',
                    nickname: '박성문',
                    profileImageUri:
                        'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
                    chat: '딥시크 짱짱',
                    timestamp: '2025-02-11T19:00:45.7557546',
                    chatType: 'MESSAGE',
                },
                hasReceivedMessage: false,
            },
            {
                type: 'sender',
                item: {
                    id: '13',
                    nickname: '박성문',
                    profileImageUri:
                        'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
                    chat: '지피티 짱짱',
                    timestamp: '2025-02-11T19:00:45.7557546',
                    chatType: 'MESSAGE',
                },
                hasReceivedMessage: true,
            },
            {
                type: 'sender',
                item: {
                    id: '14',
                    nickname: '박성문',
                    profileImageUri:
                        'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
                    chat: '클로드 짱짱',
                    timestamp: '2025-02-11T19:00:45.7557546',
                    chatType: 'MESSAGE',
                },
                hasReceivedMessage: true,
            },
            {
                type: 'receiver',
                item: {
                    id: '15',
                    nickname: '박성문',
                    profileImageUri:
                        'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
                    chat: '퍼플렉시티 짱짱',
                    timestamp: '2025-02-11T19:00:45.7557546',
                    chatType: 'MESSAGE',
                },
                hasReceivedMessage: false,
            },
            {
                type: 'sender',
                item: {
                    id: '15',
                    nickname: '박성문',
                    profileImageUri:
                        'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
                    chat: '박성문 짱짱',
                    timestamp: '2025-02-11T19:00:45.7557546',
                    chatType: 'MESSAGE',
                },
                hasReceivedMessage: false,
            },
        ],
    },
};
