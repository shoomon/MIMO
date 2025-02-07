import Tag, { TagProps } from '@/components/atoms/Tag/Tag';

const getDisplayedTags = (tagList: TagProps[]): JSX.Element[] => {
    let accumulatedLength = 0;
    const filteredTags: TagProps[] = [];

    const LIMIT_LENGTH = 14;

    for (const tag of tagList) {
        const tagLength = tag.label.length;
        if (accumulatedLength + tagLength > LIMIT_LENGTH) break;
        accumulatedLength += tagLength;
        filteredTags.push(tag);
    }

    return filteredTags.map((item: TagProps) => (
        <Tag key={item.label} to={item.to} label={item.label} />
    ));
};

export default getDisplayedTags;
