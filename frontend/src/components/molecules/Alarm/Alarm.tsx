import React from 'react';
import { useNavigate } from 'react-router-dom';

import { UserAlarmsResponse } from '@/types/User';
import { findSpecificAlarm } from '@/apis/UserAPI';
import { useModalStore } from '@/stores/modalStore';
import { useQueryClient } from '@tanstack/react-query';

export interface AlarmProps {
    active: boolean;
    addStyle: string;
    setActive: React.Dispatch<React.SetStateAction<boolean>>;
    data: UserAlarmsResponse;
}

const Alarm = ({ active, addStyle, data }: AlarmProps) => {
    const alarms = data?.userAlarms || [];
    const navigate = useNavigate();
    const queryClient = useQueryClient();

    const { openModal, closeModal } = useModalStore();

    const openAlarmModal = async (alarmId: number) => {
        try {
            const alarmData = await findSpecificAlarm(alarmId);
            console.log(alarmData);

            openModal({
                title: alarmData.alarm.title ?? '알람 상세',
                subTitle: '확인을 누르면 일정으로 이동합니다.',

                onConfirmClick: () => {
                    queryClient.invalidateQueries({
                        queryKey: ['userAlarm'],
                    });
                    closeModal();
                    navigate(
                        `/team/${alarmData.alarm.teamId}/schedule/${alarmData.alarm.scheduleId}`,
                    );
                },

                onCancelClick: () => {
                    queryClient.invalidateQueries({
                        queryKey: ['userAlarm'],
                    });
                    closeModal();
                },
            });
        } catch (error) {
            console.error('Error fetching specific alarm:', error);
        }
    };

    return (
        <section
            className={`text-text z-50 flex max-h-[300px] w-[15.5rem] origin-top transform flex-col overflow-scroll rounded-sm border border-gray-300 bg-white p-1 text-sm leading-normal font-semibold shadow-xl transition-all duration-200 ease-out ${active ? 'scale-y-100 opacity-100' : 'pointer-events-none scale-y-0 opacity-0'} ${addStyle} `}
        >
            {alarms.length === 0 ? (
                <div className="p-2">알람이 없습니다.</div>
            ) : (
                alarms.map((alarm) => (
                    <button
                        type="button"
                        onClick={() => openAlarmModal(alarm.alarmId)}
                        key={alarm.alarmId}
                        className="block flex-col justify-center border-b border-gray-200 p-2"
                    >
                        <div className="flex flex-col">
                            <div className="flex items-center justify-center truncate text-lg font-bold text-black">
                                {alarm.title}
                            </div>
                            <div className="flex justify-center font-light text-black">
                                모임에서 온 알람입니다.
                            </div>
                        </div>
                        <div className="flex items-center justify-center truncate text-lg font-bold text-black">
                            {alarm.description}
                        </div>
                        <div className="flex justify-center font-light text-black">
                            일정 리마인드
                        </div>
                    </button>
                ))
            )}
        </section>
    );
};

export default Alarm;
