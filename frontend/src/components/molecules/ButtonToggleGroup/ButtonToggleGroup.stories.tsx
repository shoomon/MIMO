// ToggleButtonGroup.stories.tsx
import type { Meta, StoryObj } from '@storybook/react';
import ButtonToggleGroup from './ButtonToggleGroup';

const meta = {
    title: 'Components/Atoms/ButtonToggleGroup',
    component: ButtonToggleGroup,
    parameters: {
        layout: 'centered',
    },
    tags: ['autodocs'],
} satisfies Meta<typeof ButtonToggleGroup>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        options: [
            { value: 'option1', label: '옵션 1' },
            { value: 'option2', label: '옵션 2' },
            { value: 'option3', label: '옵션 3' },
        ],
        defaultValue: 'option2',
        onChange: (value: string) => console.log('선택된 값:', value),
    },
};
