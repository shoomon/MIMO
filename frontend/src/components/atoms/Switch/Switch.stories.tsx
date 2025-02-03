import type { Meta, StoryObj } from '@storybook/react';
import Switch from './Switch';

const meta = {
    title: 'Components/Atoms/Switch',
    component: Switch,
    argTypes: {
        disabled: { control: 'boolean' },
        isactive: { control: 'boolean' },
        onClick: { action: 'clicked' },
    },
} satisfies Meta<typeof Switch>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Active: Story = {
    args: {
        isactive: true,
        disabled: false,
    },
};

export const Inactive: Story = {
    args: {
        isactive: false,
        disabled: false,
    },
};

export const Disabled: Story = {
    args: {
        isactive: false,
        disabled: true,
    },
};
