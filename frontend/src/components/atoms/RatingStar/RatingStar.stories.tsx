import type { Meta, StoryObj } from '@storybook/react';
import RatingStar from './RatingStar';

const meta = {
    title: 'Components/Atoms/RatingStar',
    component: RatingStar,
    parameters: {
        layout: 'centered',
    },
    argTypes: {
        rating: {
            control: { type: 'number', min: 0, max: 5, step: 0.1 },
        },
    },
} satisfies Meta<typeof RatingStar>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        reviewCount: 45,
        rating: 4.3,
    },
};

export const WithHalfStar: Story = {
    args: {
        reviewCount: 5,
        rating: 4.7,
    },
};

export const Perfect: Story = {
    args: {
        reviewCount: 2,

        rating: 5,
    },
};
