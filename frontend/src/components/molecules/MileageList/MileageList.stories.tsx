import type { Meta, StoryObj } from '@storybook/react';
import ExampleTable from './MileageList';

const meta = {
    title: 'Components/ExampleTable',
    component: ExampleTable,
    parameters: {
        layout: 'centered',
    },
    decorators: [
        (Story) => (
            <div className="h-[40vh] w-[40vw]">
                <Story />
            </div>
        ),
    ],
} satisfies Meta<typeof ExampleTable>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {},
};
