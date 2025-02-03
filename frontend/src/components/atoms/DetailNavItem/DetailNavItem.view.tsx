import { Link } from 'react-router-dom';
import Icon from '../Icon/Icon';

export interface DetailNavItemViewProps {
    item: string;
    icon: string;
    link: string;
    active: boolean;
}

const DetailNavItemView = ({
    link,
    item,
    icon,
    active,
}: DetailNavItemViewProps) => {
    const COLOR = active ? 'border-brand-primary-400' : 'border-gray-300';

    return (
        <Link
            to={link}
            className={`border-b-2 ${COLOR} hover:border-brand-primary-400 flex gap-1`}
        >
            <span
                className={`text-text ${active ? 'font-extrabold' : 'font-medium'}`}
            >
                {item}
            </span>
            <Icon type="svg" id={icon} />
        </Link>
    );
};

export default DetailNavItemView;
