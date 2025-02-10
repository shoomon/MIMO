import type { Meta, StoryObj } from '@storybook/react';
import { BrowserRouter } from 'react-router-dom';
import ChatRoom from './ChatRoom';

const meta = {
    title: 'Components/Molecules/ChatRoom',
    component: ChatRoom,
    parameters: {
        layout: 'centered',
    },
    decorators: [
        (Story) => (
            <BrowserRouter>
                <Story />
            </BrowserRouter>
        ),
    ],
} satisfies Meta<typeof ChatRoom>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        id: 2,
        imgSrc: 'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
        title: '안양 2030 동네친구모임',
        message: '내일 아침에 같이 모여서 모각코 때릴 사람',
        date: '2025년 01월 26일 오후 1:48',
        noReadCount: 21,
    },
};
