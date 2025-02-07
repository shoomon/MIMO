import type { Meta, StoryObj } from '@storybook/react';
import CardMeeting from './CardMeeting';
import { BrowserRouter } from 'react-router-dom';
import type { ThumbnailProps } from './../Thumbnail/Thumbnail.view';
import type { TagProps } from '@/components/atoms/Tag/Tag';

const meta = {
    title: 'Components/Molecules/CardMeeting',
    component: CardMeeting,
    decorators: [
        (Story) => (
            <BrowserRouter>
                <Story />
            </BrowserRouter>
        ),
    ],
    argTypes: {
        rating: { control: { type: 'number', min: 0, max: 5, step: 0.1 } },
        content: { control: 'text' },
        label: { control: 'text' },
        reviewCount: { control: 'text' },
    },
} satisfies Meta<typeof CardMeeting>;

export default meta;
type Story = StoryObj<typeof meta>;

// 더미 데이터: ThumbnailProps (실제 Thumbnail 컴포넌트의 프롭스에 맞게 수정)
const sampleThumbnail: ThumbnailProps = {
    showMember: true,
    imgSrc: 'https://randomuser.me/api/portraits/women/6.jpg',
    memberCount: 5,
    memberLimit: 10,
};

// 더미 데이터: TagProps 배열
const sampleTagList: TagProps[] = [
    { to: '/tag/react', label: '뿌빠' },
    { to: '/tag/javascript', label: '뿌빠' },
    { to: '/tag/storybook', label: '뿌빠' },
    { to: '/tag/javascript', label: '뿌빠' },
    { to: '/tag/storybook', label: '뿌빠' },
    { to: '/tag/storybook', label: '뿌빠' },
    { to: '/tag/javascript', label: '뿌빠' },
];

export const Default: Story = {
    args: {
        image: sampleThumbnail,
        rating: 4.3,
        tagList: sampleTagList,
        content:
            'This is a sample meeting card content which is supposed to be quite long to test the textCutter functionality. It should be trimmed appropriately.',
        label: 'Meeting Title',
        reviewCount: 10,
    },
};
