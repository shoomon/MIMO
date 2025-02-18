import { TagProps } from '@/components/atoms/Tag/Tag';

const tagFormatter = (tags: string[]): TagProps[] =>
    tags.map((tag) => ({
        label: tag,
        to: `search/${tag}`,
    }));

export default tagFormatter;
