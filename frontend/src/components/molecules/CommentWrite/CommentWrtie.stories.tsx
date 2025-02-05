import type { Meta, StoryObj } from '@storybook/react';
import CommentWrite from './CommentWrite';

const meta = {
    title: 'Components/Molecules/CommentWrite',
    component: CommentWrite,
    parameters: {
        layout: 'centered',
    },
} satisfies Meta<typeof CommentWrite>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {},
};
