import type { Meta, StoryObj } from '@storybook/react';
import RatingStar from './RatingStar';

const meta = {
    title: 'Components/Atoms/RatingStar',
    component: RatingStar,
    parameters: {
        layout: 'centered',
    },
    argTypes: {
        reviewScore: {
            control: { type: 'number', min: 0, max: 5, step: 0.1 },
        },
    },
} satisfies Meta<typeof RatingStar>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        reviewScore: 4.7,
        reviewCount: 0,
    },
};

export const WithHalfStar: Story = {
    args: {
        reviewScore: 4.7,
        reviewCount: 0,
    },
};

export const Perfect: Story = {
    args: {
        reviewScore: 4.7,
        reviewCount: 0,
    },
};
