import MainNavItem, {
    MainNavItemProps,
} from '@/components/atoms/MainNavItem/MainNavItem';

interface MainNavViewProps {
    navItems: MainNavItemProps[];
}

const MainNavView = ({ navItems }: MainNavViewProps) => {
    return (
        <div>
            <ul className="flex gap-6 px-1">
                {navItems.map((item) => {
                    return (
                        <li key={item.value}>
                            <MainNavItem {...item} />
                        </li>
                    );
                })}
            </ul>
        </div>
    );
};

export default MainNavView;
