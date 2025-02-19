import { getTeamQRCodeAPI } from '@/apis/QRCodeAPI';
import { getTeamInfo } from '@/apis/TeamAPI';
import { MileageContainer, MileageHistory } from '@/components/organisms';
import useTeamMileage from '@/hooks/useTeamMileage';
import { useQueryClient } from '@tanstack/react-query';
import { QRCodeCanvas } from 'qrcode.react';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const TeamMileage = () => {
    const { teamId } = useParams() as { teamId: string };
    const [qrOpen, setQrOpen] = useState<boolean>(false);
    const queryClient = useQueryClient();
    const { teamMileageData, teamMileageHistoryData } = useTeamMileage();
    const [accountNumber, setAccountNumber] = useState<string>('');
    const [QRuuid, setQRuuid] = useState<string>('');

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

    useEffect(() => {
        const fetchData = async () => {
            const data = await getTeamQRCodeAPI({ accountNumber, teamId });

            setQRuuid(data);
        };
        if (accountNumber) {
            fetchData();
        }
    }, [accountNumber, teamId]);

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
                onClick={() => {
                    setQrOpen(!qrOpen);
                }}
                className={`bg-brand-primary-300 hover:bg-brand-primary-500 absolute right-8 w-fit rounded-sm p-2 text-white`}
            >
                결제하기
            </button>
            <div
                onClick={(e) => {
                    e.stopPropagation();
                    setQrOpen(false);
                }}
                className={`fixed inset-0 flex items-center justify-center bg-gray-600/20 ${qrOpen ? 'block' : 'hidden'}`}
            >
                {QRuuid && (
                    <QRCodeCanvas
                        value={`${import.meta.env.VITE_APP_URL}pay/${QRuuid}/100/${accountNumber}/바나나우유`}
                    />
                )}
            </div>
            <MileageContainer items={teamMileageData} />
            <MileageHistory
                title="사용 내역 🧾"
                to="./payInfo"
                items={teamMileageHistoryData}
                columns={columns}
            />
        </div>
    );
};

export default TeamMileage;
