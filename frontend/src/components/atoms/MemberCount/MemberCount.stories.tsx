import type { Meta, StoryObj } from '@storybook/react';
import MemberCount from './MemberCount';

const meta = {
    title: 'Components/Atoms/MemberCount',
    component: MemberCount,
    parameters: {
        layout: 'centered',
    },
    tags: ['autodocs'],
} satisfies Meta<typeof MemberCount>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        memberCount: 3,
        memberLimit: 10,
    },
};

export const WithClick: Story = {
    args: {
        memberCount: 7,
        memberLimit: 10,
    },
};
