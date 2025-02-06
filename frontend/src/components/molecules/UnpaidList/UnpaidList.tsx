import CustomTable from '@/utils/CustomTable';
import { CellValue } from '@/utils/CustomTable';

// request 버튼 클릭 핸들러 (영어로 작성)
const requestHandler = () => {
    alert('Request handler called');
};

/**
 * RawDataRow는 외부에서 전달받은 원시 데이터 구조입니다.
 */
interface RawDataRow {
    id: number;
    name: string;
    rounds: string[]; // 회차 배열 (예: ['1회차', '2회차'])
    amount: number;
    hasRequest: boolean;
}

/**
 * DataRow는 CustomTable에서 사용하는 데이터 구조입니다.
 * CustomTable의 data prop 타입인 Record<string, CellValue>를 만족시키기 위해
 * 인덱스 시그니처를 추가합니다.
 */
interface DataRow {
    [key: string]: CellValue;
    id: number;
    name: CellValue;
    round: CellValue;
    amount: CellValue;
    request: CellValue;
}

// 외부에서 전달받은 raw 데이터 예시
const rawData: RawDataRow[] = [
    {
        id: 1,
        name: '홍길동',
        rounds: ['1회차', '2회차'],
        amount: 10000,
        hasRequest: true,
    },
    {
        id: 2,
        name: '김철수',
        rounds: ['1회차'],
        amount: 20000,
        hasRequest: false,
    },
];

/**
 * transformData 함수는 RawDataRow 배열을 받아 DataRow 배열로 변환합니다.
 * - name: 그대로 사용
 * - rounds: 배열을 콤마(,)로 구분된 문자열로 변환하여 round 필드에 할당
 * - amount: 항상 text-fail 클래스를 적용하여 { content, color } 객체로 변환
 * - hasRequest가 true면 request 필드에 클릭 가능한 버튼 객체를, 아니면 빈 문자열로 변환
 */
const transformData = (data: RawDataRow[]): DataRow[] => {
    return data.map((row) => {
        // 회차 배열을 문자열로 변환
        const roundCell: CellValue = row.rounds.join(', ');

        // 금액은 항상 text-fail 클래스를 적용
        const amountCell: CellValue = {
            content: row.amount.toLocaleString(),
            color: 'text-fail',
        };

        // 요청(request) 필드 처리: hasRequest가 true이면 버튼 객체, 아니면 빈 문자열
        const requestCell: CellValue = row.hasRequest
            ? { label: '전송', onClick: requestHandler }
            : '';

        return {
            id: row.id,
            name: row.name,
            round: roundCell,
            amount: amountCell,
            request: requestCell,
        };
    });
};

// 컬럼 정의: 헤더에 표시될 제목과 각 데이터의 키를 지정
const columns = [
    { title: '이름', dataIndex: 'name' },
    { title: '회차', dataIndex: 'round' },
    { title: '금액', dataIndex: 'amount' },
    { title: '요청', dataIndex: 'request' },
];

const UnpaidList = () => {
    const data = transformData(rawData);

    return (
        <div className="w-full overflow-hidden rounded-lg border border-gray-200">
            <CustomTable columns={columns} data={data} />
        </div>
    );
};

export default UnpaidList;
