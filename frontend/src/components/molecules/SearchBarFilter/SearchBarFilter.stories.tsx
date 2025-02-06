import type { Meta, StoryObj } from '@storybook/react';
import { BrowserRouter } from 'react-router-dom';
import SearchBarFilter from './SearchBarFilter';

const meta = {
    title: 'Components/Molecules/SearchBarFilter',
    component: SearchBarFilter,
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
} satisfies Meta<typeof SearchBarFilter>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        filterProps: {
            active: false,
            currentCondition: '제목',
            conditionList: ['제목', '인기순', '최신순'],
            onClickList: (e) => {
                alert(e.currentTarget.dataset.value);
            },
            onClick: () => {},
        },
        handleSearchSubmit: (e) => {
            e.preventDefault();
        },
        value: '',
        onChange: () => {},
    },
};
