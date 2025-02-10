import type { Meta, StoryObj } from '@storybook/react';
import { BrowserRouter } from 'react-router-dom';
import MainNavItem from './MainNavItem';

const meta = {
    title: 'Components/Atoms/MainNavItem',
    component: MainNavItem,
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
} satisfies Meta<typeof MainNavItem>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Active: Story = {
    args: {
        value: '나의 모임',
        path: '/meeting',
    },
};

export const inActive: Story = {
    args: {
        value: '나의 모임',
        path: '/meeting',
    },
};
