import { ButtonDefault } from '@/components/atoms';
import MainNavItem, {
    MainNavItemProps,
} from '@/components/atoms/MainNavItem/MainNavItem';

interface MainNavViewProps {
    navItems: MainNavItemProps[];
    createTeam: () => void;
}

const MainNavView = ({ navItems, createTeam }: MainNavViewProps) => {
    return (
        <div className="flex justify-between">
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
            <div>
                <ButtonDefault
                    content="모임 생성"
                    type="primary"
                    onClick={createTeam}
                />
            </div>
        </div>
    );
};

export default MainNavView;
