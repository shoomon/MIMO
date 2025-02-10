import type { Meta, StoryObj } from '@storybook/react';
import { BrowserRouter } from 'react-router-dom';
import Pagenation from './Pagenation';

const meta = {
    title: 'Components/Molecules/Pagenation',
    component: Pagenation,
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
} satisfies Meta<typeof Pagenation>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        currentPage: 1,
        pageSize: 15,
        onClickLeft: () => {},
        onClick: () => {},
        onClickRight: () => {},
    },
};
