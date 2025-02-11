import type { Meta, StoryObj } from '@storybook/react';
import MemberList from './MemberList';
import type { ProfileImageProps } from './../../atoms/ProfileImage/ProfileImage';

const meta = {
    title: 'Components/Molecules/MemberList',
    component: MemberList,
    argTypes: {
        userRole: {
            control: { type: 'radio' },
            options: ['owner', 'coOwner', 'member'],
        },
        bio: { control: 'text' },
        joinDate: { control: 'date' },
    },
} satisfies Meta<typeof MemberList>;

export default meta;
type Story = StoryObj<typeof meta>;

// 더미 데이터: ProfileImageProps (실제 사용하시는 데이터에 맞게 수정하세요)
const sampleProfile: ProfileImageProps = {
    userId: 'user1',
    nickname: 'John Doe',
    profileUri: 'https://randomuser.me/api/portraits/women/6.jpg',
};

export const Owner: Story = {
    args: {
        userRole: 'owner',
        userInfo: sampleProfile,
        bio: '모임장 bio sample text',
        joinDate: new Date().toISOString(),
    },
};

export const CoOwner: Story = {
    args: {
        userRole: 'coOwner',
        userInfo: sampleProfile,
        bio: '운영진 bio sample text',
        joinDate: new Date().toISOString(),
    },
};

export const Member: Story = {
    args: {
        userRole: 'member',
        userInfo: sampleProfile,
        bio: '멤버 bio sample text',
        joinDate: new Date().toISOString(),
    },
};
