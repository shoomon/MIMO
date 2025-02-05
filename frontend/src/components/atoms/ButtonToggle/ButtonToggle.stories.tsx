// ButtonToggleView.stories.tsx
import type { Meta, StoryObj } from '@storybook/react';
import ButtonToggleView from './ButtonToggle.view';

const meta = {
    title: 'Components/Atoms/ButtonToggle',
    component: ButtonToggleView,
    parameters: {
        layout: 'centered',
    },
    tags: ['autodocs'],
} satisfies Meta<typeof ButtonToggleView>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Active: Story = {
    args: {
        value: 'option1',
        active: true,
        onClick: () => alert('Active Button clicked'),
        children: 'Active Button',
    },
};

export const Inactive: Story = {
    args: {
        value: 'option2',
        active: false,
        onClick: () => alert('Inactive Button clicked'),
        children: 'Inactive Button',
    },
};
