import type { Meta, StoryObj } from '@storybook/react';
import CustomTable from './CustomTable';
import { transformData } from '@/utils/transformTableData';
import { rawTableData } from '@/mock/TeamMileageMock';

const meta = {
    title: 'Components/molecules/CustomTable',
    component: CustomTable,
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
} satisfies Meta<typeof CustomTable>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        columns: [
            { title: '내역', dataIndex: 'transaction' },
            { title: '이름', dataIndex: 'name' },
            { title: '날', dataIndex: 'date' },
            { title: '금액', dataIndex: 'amount' },
            { title: '영수증', dataIndex: 'receipt' },
        ],
        data: transformData(rawTableData),
    },
};
