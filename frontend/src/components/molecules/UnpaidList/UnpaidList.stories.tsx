import type { Meta, StoryObj } from '@storybook/react';
import UnpaidList from './UnpaidList';

const meta = {
    title: 'Components/UnpaidList',
    component: UnpaidList,
    parameters: {
        layout: 'centered',
    },
} satisfies Meta<typeof UnpaidList>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {},
};
