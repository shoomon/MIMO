import type { Meta, StoryObj } from '@storybook/react';
import MileageStatus from './MileageStatus';
import { BrowserRouter } from 'react-router-dom';

const meta = {
    title: 'Components/Atoms/MileageStatus',
    component: MileageStatus,
    decorators: [
        (Story) => (
            <BrowserRouter>
                <Story />
            </BrowserRouter>
        ),
    ],
    argTypes: {
        type: {
            control: { type: 'radio' },
            options: ['balance', 'income', 'expense'],
        },
        amount: { control: 'number' },
    },
} satisfies Meta<typeof MileageStatus>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Balance: Story = {
    args: {
        type: 'balance',
        amount: 500000,
    },
};

export const Income: Story = {
    args: {
        type: 'income',
        amount: 1500000,
    },
};

export const Expense: Story = {
    args: {
        type: 'expense',
        amount: 750000,
    },
};
