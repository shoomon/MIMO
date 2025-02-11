import type { Meta, StoryObj } from '@storybook/react';
import { action } from '@storybook/addon-actions';
import Comment from './Comment';
import type { ProfileImageProps } from './../../atoms/ProfileImage/ProfileImage';
import { BrowserRouter } from 'react-router-dom';

const meta = {
    title: 'Components/Molecules/Comment',
    component: Comment,
    argTypes: {
        commentId: { control: 'number' },
        writedate: { control: 'date' },
        content: { control: 'text' },
        isReply: { control: 'boolean' },
        onDelete: { action: 'onDelete' },
        onUpdate: { action: 'onUpdate' },
    },
    decorators: [
        (Story) => (
            <BrowserRouter>
                <Story />
            </BrowserRouter>
        ),
    ],
} satisfies Meta<typeof Comment>;

export default meta;
type Story = StoryObj<typeof meta>;

// 더미 프로필 이미지 데이터 (실제 ProfileImage 컴포넌트에 맞게 수정)
const sampleProfile: ProfileImageProps = {
    userId: 'user123',
    userName: 'Jane Doe',
    profileUri: 'https://randomuser.me/api/portraits/men/5.jpg',
};

export const Default: Story = {
    args: {
        commentId: 1,
        profileImage: sampleProfile,
        writedate: new Date().toISOString(),
        content: 'This is a sample comment.',
        isReply: false,
        name: 'John Doe',
        onDelete: action('onDelete'),
        onUpdate: action('onUpdate'),
    },
};

export const Reply: Story = {
    args: {
        commentId: 2,
        profileImage: sampleProfile,
        writedate: new Date().toISOString(),
        content: 'This is a sample reply comment.',
        name: 'John Doe',
        isReply: true,
        onDelete: action('onDelete'),
        onUpdate: action('onUpdate'),
    },
};
