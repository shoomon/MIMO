import type { Meta, StoryObj } from '@storybook/react';
import { BrowserRouter } from 'react-router-dom';
import Header from './Header';

const meta = {
    title: 'Components/Molecules/Header',
    component: Header,
    parameters: {
        layout: {
            fullscreen: true,
        },
    },
    decorators: [
        (Story) => (
            <BrowserRouter>
                <Story />
            </BrowserRouter>
        ),
    ],
} satisfies Meta<typeof Header>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        isLogin: true,
        handleSearch: () => {},
        searchValue: '',
        onChangeSearch: () => {},
        relatedItem: [],
        onClickSearch: () => {},
        handleLogin: () => {},
        setLogin: () => {},
    },
};

export const Login: Story = {
    args: {
        isLogin: true,
        handleSearch: () => {},
        searchValue: '',
        onChangeSearch: () => {},
        relatedItem: [],
        onClickSearch: () => {},
        handleLogin: () => {},
        setLogin: () => {},
    },
};

export const NoLogin: Story = {
    args: {
        isLogin: false,
        handleSearch: () => {},
        searchValue: '',
        onChangeSearch: () => {},
        relatedItem: [],
        onClickSearch: () => {},
        handleLogin: () => {},
        setLogin: () => {},
    },
};
