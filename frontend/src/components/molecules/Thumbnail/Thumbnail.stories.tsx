import type { Meta, StoryObj } from '@storybook/react';
import Thumbnail from './Thumbnail';

const meta = {
    title: 'Components/Molecules/Thumbnail',
    component: Thumbnail,
    parameters: {
        layout: 'centered',
    },
} satisfies Meta<typeof Thumbnail>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        memberCount: 5,
        memberLimit: 10,
        memberClick: () => {
            alert('모임 인원 현황으로 이동~');
        },
    },
};

export const Image: Story = {
    args: {
        memberCount: 5,
        memberLimit: 10,
        memberClick: () => {
            alert('모임 인원 현황으로 이동~');
        },
        imgSrc: 'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
        imgAlt: '박성문',
    },
};
