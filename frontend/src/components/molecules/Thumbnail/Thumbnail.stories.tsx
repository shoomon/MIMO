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
        showMember: true,
    },
};

export const Image: Story = {
    args: {
        showMember: true,
        memberCount: 5,
        memberLimit: 10,

        imgSrc: 'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
        imgAlt: '박성문',
    },
};

export const noShowMember: Story = {
    args: {
        showMember: false,
        memberCount: 0,
        memberLimit: 0,
        imgSrc: 'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
        imgAlt: '박성문',
    },
};
