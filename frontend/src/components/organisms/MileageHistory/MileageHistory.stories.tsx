import type { Meta, StoryObj } from '@storybook/react';
import { BrowserRouter } from 'react-router-dom';
import MileageHistory from './MileageHistory';

const meta = {
    title: 'Components/Organisms/MileageHistory',
    component: MileageHistory,
    decorators: [
        (Story) => (
            <BrowserRouter>
                <Story />
            </BrowserRouter>
        ),
    ],
} satisfies Meta<typeof MileageHistory>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        title: '',
        columns: [],
        items: [],
    },
};
