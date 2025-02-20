import type { Meta, StoryObj } from '@storybook/react';
import { BrowserRouter } from 'react-router-dom';
import MyInfoDropDown from './MyInfoDropDown';

const meta = {
    title: 'Components/Atoms/MyInfoDropDown',
    component: MyInfoDropDown,
    parameters: {
        layout: 'centered',
    },
    decorators: [
        (Story) => (
            <BrowserRouter>
                <Story />
            </BrowserRouter>
        ),
    ],
} satisfies Meta<typeof MyInfoDropDown>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        userInfo: {
            userId: '25',
            nickname: '박성문',
            profileUri:
                'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
        },
        active: true,
        addStyle: '',
        setActive: () => {},
        setLogin: () => {},
    },
};

export const NoData: Story = {
    args: {
        active: true,
        addStyle: '',
        setActive: () => {},
        setLogin: () => {},
    },
};
