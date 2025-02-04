import type { Meta, StoryObj } from '@storybook/react';
import ProfileImage from './ProfileImage';
import { BrowserRouter } from 'react-router-dom';

const meta = {
    title: 'Components/Atoms/ProfileImage',
    component: ProfileImage,
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
    tags: ['autodocs'],
} satisfies Meta<typeof ProfileImage>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        userId: '25',
        userName: '박성문',
    },
};

export const Current: Story = {
    args: {
        userId: '25',
        userName: '박성문',
        imgSrc: 'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
    },
};
