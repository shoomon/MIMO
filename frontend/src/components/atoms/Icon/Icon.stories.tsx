import type { Meta, StoryObj } from '@storybook/react';
import Icon from './Icon';

const meta = {
    title: 'Components/Atoms/Icon',
    component: Icon,
    parameters: {
        layout: 'centered',
    },
    tags: ['autodocs'],
} satisfies Meta<typeof Icon>;

export default meta;
type Story = StoryObj<typeof meta>;

export const SVG_Icon: Story = {
    args: {
        type: 'svg',
        id: 'MoneyTag',
    },
};

export const PNG_Icon: Story = {
    args: {
        type: 'png',
        id: 'MoneyTag',
    },
};
