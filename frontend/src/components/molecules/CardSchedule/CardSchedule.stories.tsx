import type { Meta, StoryObj } from '@storybook/react';
import CardSchedule from './CardSchedule';
import { BrowserRouter } from 'react-router-dom';

// 더미 데이터: ProfileImageProps 배열 (실제 ProfileImage 컴포넌트에 맞게 데이터를 조정하세요)
const memberListMock = [
    {
        userId: '1',
        profileUri: 'https://randomuser.me/api/portraits/men/1.jpg',
        nickname: 'User 1',
    },
    {
        userId: '2',
        profileUri: 'https://randomuser.me/api/portraits/women/2.jpg',
        nickname: 'User 2',
    },
    {
        userId: '3',
        profileUri: 'https://randomuser.me/api/portraits/men/3.jpg',
        nickname: 'User 3',
    },
    {
        userId: '4',
        profileUri: 'https://randomuser.me/api/portraits/women/4.jpg',
        nickname: 'User 4',
    },
    {
        userId: '5',
        profileUri: 'https://randomuser.me/api/portraits/men/5.jpg',
        nickname: 'User 5',
    },
    {
        userId: '6',
        profileUri: 'https://randomuser.me/api/portraits/women/6.jpg',
        nickname: 'User 6',
    },
];

const meta = {
    title: 'Components/Molecules/CardSchedule',
    component: CardSchedule,
    decorators: [
        (Story) => (
            <BrowserRouter>
                <Story />
            </BrowserRouter>
        ),
    ],
    argTypes: {
        scheduledDateTime: { control: 'date' },
        label: { control: 'text' },
        entryFee: { control: 'text' },
    },
} satisfies Meta<typeof CardSchedule>;

export default meta;
type Story = StoryObj<typeof meta>;

export const UpcomingEvent: Story = {
    args: {
        // 현재 시간 기준 3시간 뒤의 날짜를 ISO 문자열로 전달합니다.
        scheduledDateTime: new Date(
            Date.now() + 3 * 60 * 60 * 1000,
        ).toISOString(),
        label: '안녕하세요? 오늘 오실래요?',
        entryFee: 10000,
        memberList: memberListMock.slice(0, 4),
        detailLink: '456',
    },
};

export const BigEventWithManyMembers: Story = {
    args: {
        // 현재 시간 기준 2일 뒤의 날짜를 ISO 문자열로 전달합니다.
        scheduledDateTime: new Date(
            Date.now() + 2 * 24 * 60 * 60 * 1000,
        ).toISOString(),
        label: 'Big Event',
        entryFee: 5000,
        memberList: memberListMock, // 6명의 멤버가 있어, 4명 렌더링 후 "+2"가 표시됩니다.
        detailLink: '456',
    },
};
