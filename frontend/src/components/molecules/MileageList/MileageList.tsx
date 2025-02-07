import CustomTable from '@/utils/CustomTable';
import { CellValue } from '@/utils/CustomTable';

// receipt 버튼 클릭 핸들러 (영어로 작성)
const receiptHandler = () => {
    alert('Receipt handler called');
};

/**
 * RawDataRow는 외부에서 전달받은 원시 데이터 구조입니다.
 */
interface RawDataRow {
    id: number;
    transaction: string; // "출금" 또는 "입금"
    name: string;
    date: string;
    amount: number;
    hasReceipt: boolean;
}

/**
 * DataRow는 CustomTable에서 사용하는 데이터 구조입니다.
 * CustomTable의 data prop 타입인 Record<string, CellValue>를 만족시키기 위해
 * 인덱스 시그니처를 추가합니다.
 */
interface DataRow {
    [key: string]: CellValue;
    id: number;
    transaction: CellValue;
    name: CellValue;
    date: CellValue;
    amount: CellValue;
    receipt: CellValue;
}

// 외부에서 전달받은 raw 데이터 예시
const rawData: RawDataRow[] = [
    {
        id: 1,
        transaction: '출금',
        name: '홍길동',
        date: '2025-02-06',
        amount: 10000,
        hasReceipt: true,
    },
    {
        id: 2,
        transaction: '입금',
        name: '김철수',
        date: '2025-02-07',
        amount: 20000,
        hasReceipt: false,
    },
];

/**
 * transformData 함수는 RawDataRow 배열을 받아 DataRow 배열로 변환합니다.
 * - transaction 값이 "출금"이면 { content, color } 객체로 변환 (color: 'text-brand-primary-400')
 * - transaction 값이 "입금"이면 { content, color } 객체로 변환 (color: 'text-fail')
 * - hasReceipt가 true면 receipt 값을 클릭 가능한 버튼 객체로, 그렇지 않으면 빈 문자열로 변환
 */
const transformData = (data: RawDataRow[]): DataRow[] => {
    return data.map((row) => {
        let transactionCell: CellValue = row.transaction;
        if (row.transaction === '출금') {
            transactionCell = {
                content: row.transaction,
                color: 'text-brand-primary-400',
            };
        } else if (row.transaction === '입금') {
            transactionCell = { content: row.transaction, color: 'text-fail' };
        }

        const receiptCell: CellValue = row.hasReceipt
            ? { label: '영수증', onClick: receiptHandler }
            : '';

        return {
            id: row.id,
            transaction: transactionCell,
            name: row.name,
            date: row.date,
            amount: row.amount.toLocaleString(),
            receipt: receiptCell,
        };
    });
};

const columns = [
    { title: '내역', dataIndex: 'transaction' },
    { title: '이름', dataIndex: 'name' },
    { title: '날', dataIndex: 'date' },
    { title: '금액', dataIndex: 'amount' },
    { title: '영수증', dataIndex: 'receipt' },
];

const ExampleTable = () => {
    const data = transformData(rawData);

    return (
        <div className="w-full overflow-hidden rounded-lg border border-gray-200">
            <CustomTable columns={columns} data={data} />
        </div>
    );
};

export default ExampleTable;
