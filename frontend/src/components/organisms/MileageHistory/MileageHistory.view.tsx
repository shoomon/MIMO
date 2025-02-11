import { Title } from '@/components/atoms';
import CustomTable from '@/components/molecules/MileageList/CustomTable';
import { RawDataRow, transformData } from '@/utils/transformTableData';

interface MileageHistoryViewProps {
    items: RawDataRow[];
}

const MileageHistoryView = ({ items }: MileageHistoryViewProps) => {
    const columns = [
        { title: '내역', dataIndex: 'transaction' },
        { title: '이름', dataIndex: 'name' },
        { title: '날', dataIndex: 'date' },
        { title: '금액', dataIndex: 'amount' },
        { title: '영수증', dataIndex: 'receipt' },
    ];

    return (
        <section className="flex flex-col gap-6">
            <Title label="사용 내역 🧾" />
            <div className="flex gap-6">
                <CustomTable columns={columns} data={transformData(items)} />
            </div>
        </section>
    );
};

export default MileageHistoryView;
