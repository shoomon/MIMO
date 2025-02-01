// ButtonDefault.stories.tsx
import type { Meta, StoryObj } from '@storybook/react';
import ButtonDefault from './ButtonDefault';

const meta = {
    title: 'Components/Atoms/ButtonDefault',
    component: ButtonDefault,
    parameters: {
        layout: 'centered',
    },
    tags: ['autodocs'],
} satisfies Meta<typeof ButtonDefault>;

export default meta;
type Story = StoryObj<typeof meta>;

export const PrimaryWithIcon: Story = {
    args: {
        type: 'primary',
        htmlType: 'button',
        content: '확인',
        iconId: 'check-icon',
        iconType: 'svg',
    },
};

export const FailWithoutIcon: Story = {
    args: {
        type: 'fail',
        htmlType: 'button',
        content: '취소하기',
    },
};

export const DefaultWithIcon: Story = {
    args: {
        type: 'default',
        htmlType: 'button',
        content: '그냥버튼입니다.',
        iconId: 'cancelicon',
        iconType: 'svg',
    },
};
