import type { Meta, StoryObj } from '@storybook/react';
import CategoryItem from './CategoryItem';

const meta = {
    title: 'Components/Atoms/CategoryItem',
    component: CategoryItem,
    parameters: {
        layout: 'centered',
    },
    tags: ['autodocs'],
} satisfies Meta<typeof CategoryItem>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        iconId: 'Music',
        content: '음악 / 악기',
    },
};

export const Alternate: Story = {
    args: {
        iconId: 'alternateIcon',
        content: 'Alternate Category',
    },
};
