import { MileageContainer, MileageHistory } from '@/components/organisms';
import useTeamMileage from '@/hooks/useTeamMileage';
import { useState } from 'react';
import { useParams } from 'react-router-dom';

const TeamMileageNonPay = () => {
    const { teamId } = useParams() as { teamId: string; round: string };
    const [round, setRound] = useState<string>('1');
    const { teamMileageData, teamNonPayerHistoryData } = useTeamMileage(
        teamId,
        round,
    );

    const handleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setRound(e.target.value);
    };

    const columns = [
        { title: '내역', dataIndex: 'transaction' },
        { title: '멤버', dataIndex: 'user' },
        { title: '설명', dataIndex: 'name' },
        { title: '날짜', dataIndex: 'date' },
        { title: '금액', dataIndex: 'amount' },
    ];

    return (
        <div className="relative flex flex-col gap-16 px-8 py-4">
            <div className="absolute right-8 flex w-fit gap-2">
                <button
                    onClick={() => {}}
                    className={`bg-brand-primary-300 hover:bg-brand-primary-500 cursor-pointer rounded-sm p-2 text-white`}
                >
                    회비 생성하기
                </button>
                <button
                    onClick={() => {}}
                    className={`bg-brand-primary-300 hover:bg-brand-primary-500 cursor-pointer rounded-sm p-2 text-white`}
                >
                    납부하기
                </button>
                <select
                    value={round}
                    onChange={handleChange}
                    className="rounded-sm p-2"
                >
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                </select>
            </div>
            <MileageContainer items={teamMileageData} />
            <MileageHistory
                title="미납부 ❌"
                items={teamNonPayerHistoryData}
                columns={columns}
            />
        </div>
    );
};

export default TeamMileageNonPay;
