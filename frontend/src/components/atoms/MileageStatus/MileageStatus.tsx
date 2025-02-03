import { Link } from 'react-router-dom';
import Icon from '../Icon/Icon';

interface MileageStatusProps {
    type: 'balance' | 'income' | 'expense';
    amount: number;
}

const MileageStatus = ({ type, amount }: MileageStatusProps) => {
    let iconColor = '';
    let icon = '';
    let label = '';

    switch (type) {
        case 'balance':
            iconColor = 'bg-amber-100';
            icon = 'MoneyTag';
            label = '총 마일리지';
            break;

        case 'income':
            iconColor = '';
            icon = 'Group';
            label = '마일리지 수입';
            break;

        case 'expense':
            iconColor = '';
            icon = 'Medical';
            label = '마일리지 지출';
            break;
    }

    const MAX_AMOUNT_LENGTH = 1000000;

    const amountSizeFix =
        amount >= MAX_AMOUNT_LENGTH
            ? 'text-display-xs font-extrabold'
            : 'text-xl font-extrabold';

    return (
        <Link
            to={type}
            className="flex h-fit w-[255px] justify-center gap-3 rounded-2xl border-[1px] border-gray-200 bg-white px-5 py-6"
        >
            <span className={`rounded-full p-5 ${iconColor}`}>
                <Icon id={icon} size={30} type="svg" />
            </span>
            <div className="flex h-fit w-full flex-col">
                <span className="text-md font-normal text-blue-950">
                    {label}
                </span>
                <span className={amountSizeFix}>{amount}원</span>
            </div>
        </Link>
    );
};

export default MileageStatus;
