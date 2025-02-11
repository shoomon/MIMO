import type { Meta, StoryObj } from '@storybook/react';
import DetailNavItem from './DetailNavItem';
import { BrowserRouter } from 'react-router-dom';

const meta = {
    title: 'Components/Atoms/DetailNavItem',
    component: DetailNavItem,
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
} satisfies Meta<typeof DetailNavItem>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Active: Story = {
    args: {
        item: '모임소개',
        icon: 'Megaphone',
        link: '/',
    },
};

export const inActive: Story = {
    args: {
        item: '모임소개',
        icon: 'Megaphone',
        link: '/',
    },
};
