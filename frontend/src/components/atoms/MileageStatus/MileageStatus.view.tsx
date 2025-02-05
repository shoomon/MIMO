import { Link } from 'react-router-dom';
import Icon from '../Icon/Icon';

export interface MileageStatusViewProps {
    to: string;
    iconColor: string;
    icon: string;
    label: string;
    amountSizeFix: string;
    resultAmount: string;
}

const MileageStatusView: React.FC<MileageStatusViewProps> = ({
    to,
    iconColor,
    icon,
    label,
    amountSizeFix,
    resultAmount,
}) => {
    return (
        <Link
            to={to}
            className="flex h-fit w-[255px] items-center gap-3 rounded-2xl border-[1px] border-gray-200 bg-white px-5 py-6"
        >
            <span className={`rounded-full p-5 ${iconColor}`}>
                <Icon id={icon} size={30} type="svg" />
            </span>
            <div className="flex h-fit w-full flex-col gap-0">
                <span className="text-md font-normal text-blue-950">
                    {label}
                </span>
                <span className={amountSizeFix}>{resultAmount}</span>
            </div>
        </Link>
    );
};

export default MileageStatusView;
