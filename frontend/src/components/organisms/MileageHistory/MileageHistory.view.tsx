import { Title } from '@/components/atoms';
import CustomTable, {
    ColumnDefinition,
} from '@/components/molecules/MileageList/CustomTable';
import { RawDataRow, transformData } from '@/utils/transformTableData';

export interface MileageHistoryViewProps {
    title: string;
    to: string;
    columns: ColumnDefinition[];
    items: RawDataRow[];
}

const MileageHistoryView = ({
    items,
    to,
    title,
    columns,
}: MileageHistoryViewProps) => {
    return (
        <section className="flex flex-col gap-6">
            <Title label={title} to={to} />
            <div className="flex gap-6">
                <CustomTable columns={columns} data={transformData(items)} />
            </div>
        </section>
    );
};

export default MileageHistoryView;
