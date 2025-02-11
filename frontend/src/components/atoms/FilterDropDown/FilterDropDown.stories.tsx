import type { Meta, StoryObj } from '@storybook/react';
import { BrowserRouter } from 'react-router-dom';
import FilterDropDown from './FilterDropDown';

const meta = {
    title: 'Components/Atoms/FilterDropDown',
    component: FilterDropDown,
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
} satisfies Meta<typeof FilterDropDown>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        active: false,
        currentCondition: '제목',
        conditionList: ['작성자', '내용'],
        onClickList: (e) => {
            alert(e.currentTarget.dataset.value);
        },
        onClick: () => {},
    },
};
export const Active: Story = {
    args: {
        active: true,
        currentCondition: '제목',
        conditionList: ['작성자', '내용'],
        onClickList: (e) => {
            alert(e.currentTarget.dataset.value);
        },
        onClick: () => {},
    },
};
