import type { Meta, StoryObj } from '@storybook/react';
import RelatedSearchItem from './RelatedSearchItem';

const meta = {
    title: 'Components/Atoms/RelatedSearchItem',
    component: RelatedSearchItem,
    parameters: {
        layout: 'centered',
    },
    tags: ['autodocs'],
} satisfies Meta<typeof RelatedSearchItem>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        onClick: () => {
            alert('클릭!');
        },
        value: '박성문',
    },
};
