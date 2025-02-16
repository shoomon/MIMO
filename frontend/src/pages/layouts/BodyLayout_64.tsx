import { ReactNode } from 'react';

export interface BodyLayout64Props {
    children: ReactNode;
}

const BodyLayout_64 = ({ children }: BodyLayout64Props) => {
    return (
        <div className="flex h-fit w-full flex-col items-center gap-16 bg-gray-50 py-4 pl-8">
            {children}
        </div>
    );
};

export default BodyLayout_64;
