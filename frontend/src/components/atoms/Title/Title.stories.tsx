import type { Meta, StoryObj } from '@storybook/react';
import Title from './Title';
import { BrowserRouter } from 'react-router-dom';

const meta = {
    title: 'Components/Atoms/Title',
    component: Title,
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
        onClick: { action: 'clicked' },
    },
} satisfies Meta<typeof Title>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        label: 'Home',
    },
};

export const WithLink: Story = {
    args: {
        label: 'Go to About',
        to: '/about',
    },
};

export const Clickable: Story = {
    args: {
        label: 'Click Me',
        onClick: () => alert('Title clicked!'),
    },
};
