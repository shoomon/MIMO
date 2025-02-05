import type { Meta, StoryObj } from '@storybook/react';
import Search from './Search';

const meta = {
    title: 'Components/Atoms/Search',
    component: Search,
    parameters: {
        layout: 'centered',
    },
    tags: ['autodocs'],
} satisfies Meta<typeof Search>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        handleSubmit: (e) => {
            e.preventDefault();
            console.log(e);
        },
        value: '',
        onChange: (e) => {
            console.log(e.target.value);
        },
    },
};
