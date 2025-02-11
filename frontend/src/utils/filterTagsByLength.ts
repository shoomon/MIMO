import { TagProps } from '@/components/atoms/Tag/Tag';

const filterTagsByLength = (tags: TagProps[]): TagProps[] => {
    let accumulatedLength = 0;
    const filteredTags: TagProps[] = [];

    for (const tag of tags) {
        const tagLength = tag.label.length;
        if (accumulatedLength + tagLength > 14) break;
        accumulatedLength += tagLength;
        filteredTags.push(tag);
    }

    return filteredTags;
};

export default filterTagsByLength;
