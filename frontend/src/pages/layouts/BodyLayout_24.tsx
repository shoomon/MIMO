import { BodyLayout64Props } from './BodyLayout_64';

const BodyLayout_24 = ({ children }: BodyLayout64Props) => {
    return (
        <div className="flex h-fit w-full flex-col items-center gap-6 bg-gray-50 py-4 pt-4 pr-3 pl-8">
            {children}
        </div>
    );
};

export default BodyLayout_24;
