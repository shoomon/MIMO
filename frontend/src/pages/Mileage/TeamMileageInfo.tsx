import { MileageContainer, MileageHistory } from '@/components/organisms';
import useTeamMileage from '@/hooks/useTeamMileage';
import { useState } from 'react';
import { useParams } from 'react-router-dom';

const TeamMileageInfo = () => {
    const { teamId } = useParams() as { teamId: string };
    const [round, setRound] = useState<string>('1');
    const { teamMileageData, teamMileageHistoryData } = useTeamMileage(
        teamId,
        round,
    );

    const handleChange = (e) => {
        setRound(e.target.value);
    };

    const columns = [
        { title: '내역', dataIndex: 'transaction' },
        { title: '설명', dataIndex: 'name' },
        { title: '날짜', dataIndex: 'date' },
        { title: '금액', dataIndex: 'amount' },
    ];

    return (
        <div className="relative flex flex-col gap-16 px-8 py-4">
            <select
                value={round}
                onChange={handleChange}
                className="absolute right-8 w-fit rounded-sm p-2"
            >
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
            </select>
            <MileageContainer items={teamMileageData} />
            <MileageHistory
                title="회비 납부 내역 🧾"
                to={`/team/${teamId}/mileage/payment`}
                items={teamMileageHistoryData}
                columns={columns}
            />
            <MileageHistory
                title="미납부 ❌"
                to={`/team/${teamId}/mileage/non-payment`}
                items={teamMileageHistoryData}
                columns={columns}
            />
        </div>
    );
};

export default TeamMileageInfo;
