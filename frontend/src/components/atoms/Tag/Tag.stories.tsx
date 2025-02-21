import type { Meta, StoryObj } from '@storybook/react';
import Tag from './Tag';
import { BrowserRouter } from 'react-router-dom';

const meta = {
    title: 'Components/Atoms/Tag',
    component: Tag,
    decorators: [
        (Story) => (
            <BrowserRouter>
                <Story />
            </BrowserRouter>
        ),
    ],
    argTypes: {
        label: { control: 'text' },
    },
} satisfies Meta<typeof Tag>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        label: 'Sample Tag',
    },
};
