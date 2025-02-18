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
        images: [
            {
                teamBoardId: 'string',
                boardId: 'string',
                imageUri:
                    'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            },
            {
                teamBoardId: 'string',
                boardId: 'string',
                imageUri:
                    'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            },
            {
                teamBoardId: 'string',
                boardId: 'string',
                imageUri:
                    'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            },
            {
                teamBoardId: 'string',
                boardId: 'string',
                imageUri:
                    'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            },
            {
                teamBoardId: 'string',
                boardId: 'string',
                imageUri:
                    'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            },
            {
                teamBoardId: 'string',
                boardId: 'string',
                imageUri:
                    'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            },
        ],
    },
};
