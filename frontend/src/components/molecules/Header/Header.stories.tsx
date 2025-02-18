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
        alarmActive: false,
        infoActive: true,
        handleSearch: () => {},
        searchValue: '',
        onChangeSearch: () => {},
        relatedItem: [],
        onClickSearch: () => {},
        onClickInfo: () => {},
        handleLogin: () => {},
        setLogin: () => {},
        setActive: () => {},
    },
};

export const Login: Story = {
    args: {
        isLogin: true,
        alarmActive: false,
        infoActive: true,
        handleSearch: () => {},
        searchValue: '',
        onChangeSearch: () => {},
        relatedItem: [],
        onClickSearch: () => {},
        onClickInfo: () => {},
        handleLogin: () => {},
        setLogin: () => {},
        setActive: () => {},
    },
};

export const NoLogin: Story = {
    args: {
        isLogin: false,
        alarmActive: false,
        infoActive: true,
        handleSearch: () => {},
        searchValue: '',
        onChangeSearch: () => {},
        relatedItem: [],
        onClickSearch: () => {},
        onClickInfo: () => {},
        handleLogin: () => {},
        setLogin: () => {},
        setActive: () => {},
    },
};
