import type { Meta, StoryObj } from '@storybook/react';
import ParticipantName from './ParticipantName';

const meta = {
    title: 'Components/Atoms/ParticipantName',
    component: ParticipantName,
    parameters: {
        layout: 'centered',
    },
    tags: ['autodocs'],
} satisfies Meta<typeof ParticipantName>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        name: '박성문',
        onClick: () => {
            console.log('클릭!');
        },
    },
};
