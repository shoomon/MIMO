import { ButtonDefault } from '@/components/atoms';
import { CellValue } from '@/utils/transformTableData';

// export default ExampleTable;
interface ColumnDefinition {
    /** 컬럼 제목 (헤더에 표시됨) */
    title: string;
    /** 데이터 객체에서 해당 컬럼에 접근할 키 */
    dataIndex: string;
    /** (옵션) 컬럼의 너비 */
    width?: number | string;
    /**
     * (옵션) 컬럼의 기본 텍스트 색상
     * 헤더(title)에는 적용되지 않고, 각 셀의 내용에만 적용됩니다.
     *
     * 만약 셀 데이터에 별도의 스타일(예, color)이 있다면 우선순위는 셀 데이터에 따릅니다.
     */
    textColor?: string;
}

interface CustomTableProps {
    /** 컬럼 정의 배열 */
    columns: ColumnDefinition[];
    /** 테이블에 표시할 데이터 배열 */
    data: Record<string, CellValue>[];
}

const CustomTable: React.FC<CustomTableProps> = ({ columns, data }) => {
    /**
     * 클릭 가능한 셀임을 판별하는 타입 가드 함수
     */
    const isClickableCell = (
        value: CellValue,
    ): value is { label: string; onClick: () => void } =>
        typeof value === 'object' && value !== null && 'onClick' in value;

    /**
     * 스타일이 적용된 일반 텍스트 셀임을 판별하는 타입 가드 함수
     * (즉, content 속성이 있으면 스타일 적용 객체로 판단)
     */
    const isStyledCell = (
        value: CellValue,
    ): value is { content: string | number; color?: string } =>
        typeof value === 'object' && value !== null && 'content' in value;

    return (
        <div className="w-full overflow-hidden rounded-lg border border-gray-200">
            <table className="w-full table-auto">
                <thead>
                    <tr>
                        {columns.map((col, colIndex) => (
                            <th
                                key={colIndex}
                                className="text-md rounded-lg border-b border-gray-200 bg-white px-[24px] py-[12px] text-left font-medium"
                            >
                                {col.title}
                            </th>
                        ))}
                    </tr>
                </thead>
                <tbody>
                    {data.map((row, rowIndex) => {
                        // row.id가 존재하고 string 또는 number 타입이면 키로 사용, 아니면 rowIndex 사용
                        const rowKey =
                            typeof row.id === 'string' ||
                            typeof row.id === 'number'
                                ? row.id
                                : rowIndex;
                        return (
                            <tr key={rowKey} className="border-t">
                                {columns.map((col, colIndex) => {
                                    const cell = row[col.dataIndex];
                                    /**
                                     * 우선: 셀 데이터 자체에 color가 있으면 해당 클래스를 사용하고,
                                     * 그렇지 않으면 컬럼 정의의 textColor를 사용합니다.
                                     */
                                    let cellClass = '';
                                    if (isStyledCell(cell) && cell.color) {
                                        cellClass = cell.color;
                                    } else if (
                                        !isStyledCell(cell) &&
                                        col.textColor
                                    ) {
                                        cellClass = col.textColor;
                                    }

                                    if (isClickableCell(cell)) {
                                        return (
                                            <td
                                                key={colIndex}
                                                className="cursor-pointer border-t border-b border-gray-200 px-[24px] py-[21.5px] hover:bg-gray-50"
                                            >
                                                <ButtonDefault
                                                    type="default"
                                                    onClick={cell.onClick}
                                                    htmlType="button"
                                                    content={'다운로드'}
                                                />
                                            </td>
                                        );
                                    } else if (isStyledCell(cell)) {
                                        return (
                                            <td
                                                key={colIndex}
                                                className="border-t border-b border-gray-200 p-2 px-[24px] py-[21.5px] text-left"
                                            >
                                                <span className={cellClass}>
                                                    {cell.content}
                                                </span>
                                            </td>
                                        );
                                    } else {
                                        return (
                                            <td
                                                key={colIndex}
                                                className="border-t border-b border-gray-200 p-2 px-[24px] py-[21.5px] text-left"
                                            >
                                                <span className={cellClass}>
                                                    {cell}
                                                </span>
                                            </td>
                                        );
                                    }
                                })}
                            </tr>
                        );
                    })}
                </tbody>
            </table>
        </div>
    );
};

export default CustomTable;
