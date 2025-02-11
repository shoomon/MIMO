import type { Meta, StoryObj } from '@storybook/react';
import { BrowserRouter } from 'react-router-dom';
import NavLevel from './NavLevel';

const meta = {
    title: 'Components/Molecules/NavLevel',
    component: NavLevel,
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
} satisfies Meta<typeof NavLevel>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {},
};
