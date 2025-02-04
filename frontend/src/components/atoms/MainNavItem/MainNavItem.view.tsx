import { Link } from 'react-router-dom';

export interface MainNavItemViewProps {
    value: string;
    path: string;
    active: boolean;
}

const MainNavItemView = ({ value, path, active }: MainNavItemViewProps) => {
    return (
        <Link
            to={path}
            className={`text-dark border-b-2 py-3 font-semibold ${active ? 'border-brand-primary-400' : 'border-transparent'} hover:border-brand-primary-200 hover:opacity-60`}
        >
            {value}
        </Link>
    );
};

export default MainNavItemView;
