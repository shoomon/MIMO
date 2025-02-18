import type { Meta, StoryObj } from '@storybook/react';
import CardBoard from './CardBoard';
import { BrowserRouter } from 'react-router-dom';
import type { ThumbnailProps } from '../Thumbnail/Thumbnail.view';

export interface CardBoardProps {
    userProfileUri: string;
    userNickname: string;
    postTitle: string;
    imageUri: string;
    likeCount: number;
    viewCount: number;
    commentCount: number;
    createdAt: string;
    updatedAt: string;
    layoutType: 'List' | 'Card';
    linkto: string;
}

const meta = {
    title: 'Components/Molecules/CardBoard',
    component: CardBoard,
    decorators: [
        (Story) => (
            <BrowserRouter>
                <Story />
            </BrowserRouter>
        ),
    ],
    argTypes: {
        userNickname: { control: 'text' },
        postTitle: { control: 'text' },
        likeCount: { control: 'number' },
        viewCount: { control: 'number' },
        commentCount: { control: 'number' },
        createdAt: { control: 'date' },
        updatedAt: { control: 'date' },
        layoutType: { control: { type: 'radio' }, options: ['List', 'Card'] },
        linkto: { control: 'text' },
        imageUri: { table: { disable: true } },
    },
} satisfies Meta<typeof CardBoard>;

export default meta;
type Story = StoryObj<typeof meta>;

const sampleThumbnail: ThumbnailProps = {
    showMember: false,
    imgSrc: 'https://randomuser.me/api/portraits/men/3.jpg',
};

export const CardLayout: Story = {
    args: {
        userProfileUri: 'https://randomuser.me/api/portraits/women/1.jpg',
        userNickname: 'John Doe',
        postTitle: 'Card Layout Post',
        imageUri: sampleThumbnail.imgSrc!,
        likeCount: 123,
        viewCount: 456,
        commentCount: 10,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        layoutType: 'Card',
        linkto: '/post/1',
    },
};

export const ListLayout: Story = {
    args: {
        userProfileUri: 'https://randomuser.me/api/portraits/men/2.jpg',
        userNickname: 'Jane Doe',
        postTitle: 'List Layout Post',
        imageUri: sampleThumbnail.imgSrc!,
        likeCount: 234,
        viewCount: 567,
        commentCount: 15,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        layoutType: 'List',
        linkto: '/post/2',
    },
};
