import type { Meta, StoryObj } from '@storybook/react';
import CardBoard from './CardBoard';
import { BrowserRouter } from 'react-router-dom';
import type { ThumbnailProps } from '../Thumbnail/Thumbnail.view';

const meta = {
    title: 'Components/Molecules/CardBoard',
    component: CardBoard,
    decorators: [
        (Story) => (
            <BrowserRouter>
                <Story />
            </BrowserRouter>
        ),
    ],
    argTypes: {
        label: { control: 'text' },
        author: { control: 'text' },
        date: { control: 'date' },
        viewCount: { control: 'number' },
        layoutType: { control: { type: 'radio' }, options: ['List', 'Card'] },
        // image는 ThumbnailProps 타입이므로, 스토리북 컨트롤에서는 직접 다루지 않도록 합니다.
        image: { table: { disable: true } },
    },
} satisfies Meta<typeof CardBoard>;

export default meta;
type Story = StoryObj<typeof meta>;

// Thumbnail에 필요한 더미 데이터 (실제 Thumbnail 컴포넌트의 프롭스에 맞게 수정하세요)
const sampleThumbnail: ThumbnailProps = {
    showMember: false,
    imgSrc: 'https://randomuser.me/api/portraits/men/3.jpg',
};

export const CardLayout: Story = {
    args: {
        image: sampleThumbnail,
        label: 'Card Layout Post',
        author: 'John Doe',
        date: new Date().toISOString(),
        viewCount: 123,
        layoutType: 'Card',
    },
};

export const ListLayout: Story = {
    args: {
        image: sampleThumbnail,
        label: 'List Layout Post',
        author: 'Jane Doe',
        date: new Date().toISOString(),
        viewCount: 456,
        layoutType: 'List',
    },
};
