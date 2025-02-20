/**
 * 셀 데이터 타입
 * - string | number: 단순 값
 * - { content, color? }: 일반 텍스트에 스타일(예, Tailwind 클래스)를 적용
 * - { label, onClick }: 클릭 가능한 셀
 */
export type CellValue =
    | string
    | number
    | {
          content: string | number;
          color?: string;
      }
    | {
          label: string;
          onClick: () => void;
      };

/**
 * RawDataRow는 외부에서 전달받은 원시 데이터 구조입니다.
 */
export interface RawDataRow {
    id: number;
    user: string;
    transaction: string; // "출금" 또는 "입금"
    name: string;
    date: string;
    amount: number;
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
    user: CellValue;
}

/**
 * transformData 함수는 RawDataRow 배열을 받아 DataRow 배열로 변환합니다.
 * - transaction 값이 "출금"이면 { content, color } 객체로 변환 (color: 'text-brand-primary-400')
 * - transaction 값이 "입금"이면 { content, color } 객체로 변환 (color: 'text-fail')
 * - hasReceipt가 true면 receipt 값을 클릭 가능한 버튼 객체로, 그렇지 않으면 빈 문자열로 변환
 */

export const transformData = (data: RawDataRow[]): DataRow[] => {
    if (data == undefined) {
        return [];
    }

    return data.map((row) => {
        let transactionCell: CellValue = row.transaction;
        if (row.transaction === '출금') {
            transactionCell = {
                content: row.transaction,
                color: 'text-brand-primary-400',
            };
        } else if (
            row.transaction === '입금' ||
            row.transaction === '지출' ||
            row.transaction === '미납부'
        ) {
            transactionCell = { content: row.transaction, color: 'text-fail' };
        }

        return {
            id: row.id,
            transaction: transactionCell,
            name: row.name,
            date: row.date,
            amount: row.amount.toLocaleString(),
            user: row.user,
        };
    });
};
