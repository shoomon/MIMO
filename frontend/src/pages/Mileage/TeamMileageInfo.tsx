import { getTeamInfo } from '@/apis/TeamAPI';
import { MileageContainer, MileageHistory } from '@/components/organisms';
import TeamInstallmentModal from '@/components/organisms/TeamInstallmentModal/TeamInstallmentModal';
import useTeamMileage from '@/hooks/useTeamMileage';
import { useQueryClient } from '@tanstack/react-query';
import { useEffect, useMemo, useState } from 'react';
import { useParams } from 'react-router-dom';

const TeamMileageInfo = () => {
    const { teamId } = useParams() as { teamId: string };
    const queryClient = useQueryClient();
    const [round, setRound] = useState<string>('0');
    const [amount, setAmount] = useState<number>(0);
    const [accountNumber, setAccountNumber] = useState<string>('');
    const [installOpen, setInstallOpen] = useState<boolean>(false);
    const {
        teamCurrentRound,
        teamMileageData,
        teamNonPayerHistoryShortData,
        teamPayerHistoryShortData,
        teamInfo,
        myPayCheck,
    } = useTeamMileage(teamId, round);

    useEffect(() => {
        if (teamCurrentRound == undefined) {
            return;
        }

        setRound(String(teamCurrentRound + 1));

        console.log('현재 라운드', teamCurrentRound + 1);
    }, [teamCurrentRound]);

    useEffect(() => {
        const fetchData = async () => {
            const data = await queryClient.fetchQuery({
                queryKey: ['teamInfo', teamId],
                queryFn: () => getTeamInfo(teamId),
            });
            setAccountNumber(data.accountNumber);
        };

        fetchData();
    }, []);

    const teamMemberData = useMemo(() => {
        if (!teamInfo?.users) return [];

        return teamInfo.users.map((item, index) => {
            return {
                key: index,
                nickname: item.nickname,
                selected: false,
                profileUrl: item.profileUri,
                userId: String(item.userId),
                transferRequest: null,
            };
        });
    }, [teamInfo]);

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

    const nonPayColumn = [
        { title: '내역', dataIndex: 'transaction' },
        { title: '멤버', dataIndex: 'user' },
        { title: '금액', dataIndex: 'amount' },
    ];

    return (
        <div className="relative flex flex-col gap-16 px-8 py-4">
            <div className="absolute right-8 flex w-fit gap-2">
                <button
                    onClick={() => {
                        setInstallOpen(true);
                    }}
                    className={`bg-brand-primary-300 hover:bg-brand-primary-500 cursor-pointer rounded-sm p-2 text-white`}
                >
                    회비 생성하기
                </button>
                {myPayCheck && (
                    <button
                        onClick={() => {}}
                        className={`bg-brand-primary-300 hover:bg-brand-primary-500 cursor-pointer rounded-sm p-2 text-white`}
                    >
                        납부하기
                    </button>
                )}
                <select
                    value={round}
                    onChange={handleChange}
                    className="rounded-sm p-2"
                >
                    {teamCurrentRound !== undefined &&
                        Array.from({ length: teamCurrentRound + 1 }, (_, i) => (
                            <option key={i} value={i + 1}>
                                {i + 1}
                            </option>
                        ))}
                </select>
            </div>
            <div
                onClick={() => {
                    setInstallOpen(false);
                }}
                className={`fixed inset-0 flex items-center justify-center bg-gray-600/20 ${installOpen ? 'block' : 'hidden'}`}
            >
                {teamMemberData && (
                    <TeamInstallmentModal
                        teamId={teamId}
                        round={round}
                        members={teamMemberData}
                        clickClose={setInstallOpen}
                    />
                )}
            </div>
            <MileageContainer items={teamMileageData} />
            <MileageHistory
                title="회비 납부 내역 🧾"
                to={`/team/${teamId}/mileage/payment`}
                items={teamPayerHistoryShortData}
                columns={columns}
            />
            <MileageHistory
                title="미납부 ❌"
                to={`/team/${teamId}/mileage/non-payment`}
                items={teamNonPayerHistoryShortData}
                columns={nonPayColumn}
            />
        </div>
    );
};

export default TeamMileageInfo;
