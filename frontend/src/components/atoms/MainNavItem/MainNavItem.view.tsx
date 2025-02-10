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
                `text-dark inline-block border-b-2 pb-2 font-semibold ${
                    isActive ? 'border-brand-primary-400' : 'border-transparent'
                } hover:border-brand-primary-200 inline-block hover:opacity-60`
            }
        >
            {value}
        </NavLink>
    );
};

export default MainNavItemView;
