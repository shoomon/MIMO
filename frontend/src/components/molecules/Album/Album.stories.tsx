import type { Meta, StoryObj } from '@storybook/react';
import { BrowserRouter } from 'react-router-dom';
import Album from './Album';

const meta = {
    title: 'Components/Molecules/Album',
    component: Album,
    decorators: [
        (Story) => (
            <BrowserRouter>
                <Story />
            </BrowserRouter>
        ),
    ],
} satisfies Meta<typeof Album>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        id: '23',
        items: [
            {
                imgId: '12',
                imgSrc: 'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            },
            {
                imgId: '13',
                imgSrc: 'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            },
            {
                imgId: '14',
                imgSrc: 'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            },
            {
                imgId: '15',
                imgSrc: 'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            },
            {
                imgId: '16',
                imgSrc: 'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            },
            {
                imgId: '17',
                imgSrc: 'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            },
        ],
    },
};
