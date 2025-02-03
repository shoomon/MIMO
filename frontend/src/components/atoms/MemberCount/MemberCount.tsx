import Icon from '../Icon/Icon';

interface MemberCountProps {
    memberLimit: string;
    memberCount: string;
    onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const MemberCount = ({
    memberLimit,
    memberCount,
    onClick,
}: MemberCountProps) => {
    return (
        <button
            onClick={onClick}
            className="h-fit w-fit rounded-sm border-1 border-gray-500 px-2 py-1"
        >
            <div className="flex gap-1">
                <Icon type="svg" id="User" />
                <div className="flex justify-center text-sm font-semibold text-gray-700">
                    {memberCount}/{memberLimit}
                </div>
            </div>
        </button>
    );
};

export default MemberCount;
