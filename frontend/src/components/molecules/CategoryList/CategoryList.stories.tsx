import type { Meta, StoryObj } from '@storybook/react';
import CategoryList from './CategoryList';
import { BrowserRouter } from 'react-router-dom';

const meta = {
    title: 'Components/Molecules/CategoryList',
    component: CategoryList,
    parameters: {
        layout: 'centered',
    },
    decorators: [
        (Story) => (
            <BrowserRouter>
                <div className="w-3xs">
                    <Story />
                </div>
            </BrowserRouter>
        ),
    ],
} satisfies Meta<typeof CategoryList>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {},
};
