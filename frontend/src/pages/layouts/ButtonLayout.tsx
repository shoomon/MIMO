import { ReactNode } from 'react';

export interface BaseLayoutprops {
    children: ReactNode;
}

const ButtonLayout = ({ children }: BaseLayoutprops) => {
    return (
        <section className="flex min-h-[51px] items-start justify-end gap-3 self-stretch py-2">
            {children}
        </section>
    );
};

export default ButtonLayout;
