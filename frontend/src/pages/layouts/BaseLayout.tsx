import { ReactNode } from 'react';

export interface BaseLayoutprops {
    children: ReactNode;
}

const BaseLayout = ({ children }: BaseLayoutprops) => {
    return <section className="flex flex-col gap-2">{children}</section>;
};

export default BaseLayout;
