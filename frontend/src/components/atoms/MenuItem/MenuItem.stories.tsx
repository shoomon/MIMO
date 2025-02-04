import type { Meta, StoryObj } from '@storybook/react';
import MenuItem from './MenuItem';
import { BrowserRouter } from 'react-router-dom';

const meta = {
    title: 'Components/Atoms/MenuItem',
    component: MenuItem,
    decorators: [
        (Story) => (
            <BrowserRouter>
                <Story />
            </BrowserRouter>
        ),
    ],
    argTypes: {
        label: { control: 'text' },
        to: { control: 'text' },
    },
} satisfies Meta<typeof MenuItem>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        label: 'Home',
        to: '/',
    },
};

export const About: Story = {
    args: {
        label: 'About',
        to: '/about',
    },
};
