import type { Meta, StoryObj } from '@storybook/react';
import MeetingInfo from './MeetingInfo';
import { BrowserRouter } from 'react-router-dom';

const meta = {
    title: 'Components/Molecules/MeetingInfo',
    component: MeetingInfo,
    argTypes: {
        subTitle: { control: 'text' },
        title: { control: 'text' },
        reviewScore: { control: 'object' },
        tag: { control: 'object' },
        maxCapacity: { control: 'object' },
    },
    decorators: [
        (Story) => (
            <BrowserRouter>
                <Story />
            </BrowserRouter>
        ),
    ],
} satisfies Meta<typeof MeetingInfo>;

export default meta;
type Story = StoryObj<typeof meta>;

// 더미 데이터: TagProps 배열
const sampleTagList = [
    { to: '/tag/react', label: 'React' },
    { to: '/tag/javascript', label: 'JavaScript' },
    { to: '/tag/storybook', label: 'Storybook' },
];

export const Default: Story = {
    args: {
        teamId: '123',
        subTitle: 'Meeting Subtitle Sample',
        reviewScore: 0,
        reviewCount: 0,
        title: 'Meeting Title Sample',
        tag: sampleTagList,
        maxCapacity: 12,
        currentCapacity: 6,
        teamUserId: 2,
        recruitStatus: 'ACTIVE_PUBLIC',
        notificationStatus: 'ACTIVE',
    },
};
