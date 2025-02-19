import { MileageContainer, MileageHistory } from '@/components/organisms';
import useTeamMileage from '@/hooks/useTeamMileage';
import { useParams } from 'react-router-dom';

const TeamMileagePay = () => {
    const { teamId } = useParams() as { teamId: string };
    const { teamMileageData, teamMileageHistoryData } = useTeamMileage();

    const columns = [
        { title: '내역', dataIndex: 'transaction' },
        { title: '설명', dataIndex: 'name' },
        { title: '날짜', dataIndex: 'date' },
        { title: '금액', dataIndex: 'amount' },
        { title: '영수증', dataIndex: 'receipt' },
    ];

    return (
        <div className="relative flex flex-col gap-16 px-8 py-4">
            <button
                className={`bg-brand-primary-300 hover:bg-brand-primary-500 absolute right-8 w-fit rounded-sm p-2 text-white`}
            >
                결제하기
            </button>
            <MileageContainer items={teamMileageData} />
            <MileageHistory
                title="회비 납부 내역 🧾"
                to={`/team/${teamId}/mileage/payment`}
                items={teamMileageHistoryData}
                columns={columns}
            />
        </div>
    );
};

export default TeamMileagePay;
