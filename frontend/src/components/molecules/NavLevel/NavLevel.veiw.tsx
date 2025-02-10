import { Icon } from '@/components/atoms';
import Title, { TitleProps } from '@/components/atoms/Title/Title';

export interface NavLevelProps {
    navItems: TitleProps[];
}

const NavLevelView = ({ navItems }: NavLevelProps) => {
    return (
        <nav className="flex items-center gap-2">
            <Icon type="svg" id="Arrow-Back" />
            {navItems.map((item) => {
                return <Title label={item.label} to={item.to} />;
            })}
        </nav>
    );
};

export default NavLevelView;
