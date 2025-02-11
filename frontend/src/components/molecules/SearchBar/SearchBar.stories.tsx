import type { Meta, StoryObj } from '@storybook/react';
import SearchBar from './SearchBar';

const meta = {
    title: 'Components/Molecules/SearchBar',
    component: SearchBar,
    parameters: {
        layout: 'centered',
    },
    tags: ['autodocs'],
} satisfies Meta<typeof SearchBar>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        handleSubmit: () => {},
        value: '',
        onChange: (e) => {
            console.log(e.target.value);
        },
        relatedItem: [],
        onClick: (e) => {
            alert(e.currentTarget.dataset.value);
        },
    },
};

export const Search: Story = {
    args: {
        handleSubmit: () => {},
        value: '',
        onChange: (e) => {
            console.log(e.target.value);
        },
        relatedItem: ['양념치킨', '후라이드치킨', '마늘치킨', '뿌링클'],
        onClick: (e) => {
            alert(e.currentTarget.dataset.value);
        },
    },
};
