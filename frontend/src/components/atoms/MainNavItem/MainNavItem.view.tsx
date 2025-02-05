import { NavLink } from 'react-router-dom';

export interface MainNavItemViewProps {
    value: string;
    path: string;
}

const MainNavItemView = ({ value, path }: MainNavItemViewProps) => {
    return (
        <NavLink
            to={path}
            className={({ isActive }) =>
                `text-dark border-b-2 py-3 font-semibold ${
                    isActive ? 'border-brand-primary-400' : 'border-transparent'
                } hover:border-brand-primary-200 hover:opacity-60`
            }
        >
            {value}
        </NavLink>
    );
};

export default MainNavItemView;
