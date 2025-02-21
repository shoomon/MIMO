import MileageStatusView from './MileageStatus.view';

/**
 * MileageStatus 컴포넌트의 props 타입 정의
 */
export interface MileageStatusProps {
    /** 마일리지 상태 유형 ('balance' | 'income' | 'expense') */
    type: 'balance' | 'income' | 'expense';
    /** 마일리지 금액 */
    amount: number;
}

/**
 * 마일리지 상태를 표시하는 컴포넌트
 *
 * @param {MileageStatusProps} props - 컴포넌트 props
 * @param {'balance' | 'income' | 'expense'} props.type - 마일리지 유형 (잔액, 수입, 지출)
 * @param {number} props.amount - 마일리지 금액
 *
 * @returns {JSX.Element} 마일리지 상태 UI를 렌더링하는 React 컴포넌트
 */
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
            iconColor = 'bg-blue-100';
            icon = 'Income';
            label = '마일리지 수입';
            break;

        case 'expense':
            iconColor = 'bg-red-100';
            icon = 'Expense';
            label = '마일리지 지출';
            break;
    }

    /** 특정 금액 이상이면 폰트 크기를 조정 */
    const MAX_AMOUNT_LENGTH = 1000000;
    const amountSizeFix =
        amount >= MAX_AMOUNT_LENGTH
            ? 'text-lg font-extrabold'
            : 'text-display-xs font-extrabold';

    /** 금액을 천 단위로 포맷팅 */
    const formattedNumber = amount.toLocaleString();
    const resultAmount = Number.isNaN(amount)
        ? '---원'
        : `${formattedNumber}원`;

    return (
        <MileageStatusView
            iconColor={iconColor}
            icon={icon}
            label={label}
            amountSizeFix={amountSizeFix}
            resultAmount={resultAmount}
        />
    );
};

export default MileageStatus;
