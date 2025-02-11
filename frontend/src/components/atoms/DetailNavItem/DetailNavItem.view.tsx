import { NavLink } from 'react-router-dom';
import Icon from '../Icon/Icon';

export interface DetailNavItemViewProps {
    item: string;
    icon: string;
    link: string;
}

const DetailNavItemView = ({ link, item, icon }: DetailNavItemViewProps) => {
    return (
        <NavLink
            to={link}
            className={({ isActive }) =>
                `hover:border-brand-primary-400 flex gap-1 border-b-2 ${isActive ? 'border-brand-primary-400 font-extrabold' : 'border-gray-300 font-medium'}`
            }
        >
            <span className="text-text">{item}</span>
            <Icon type="svg" id={icon} />
        </NavLink>
    );
};

export default DetailNavItemView;
