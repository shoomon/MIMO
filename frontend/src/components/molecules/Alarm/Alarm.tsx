// src/components/atoms/Alarm.tsx
import { UserAlarmsResponse } from '@/types/User';

export interface AlarmProps {
    active: boolean;
    addStyle: string;
    setActive: React.Dispatch<React.SetStateAction<boolean>>;
    data: UserAlarmsResponse;
}

const Alarm = ({ active, addStyle, data }: AlarmProps) => {
    const alarms = data?.userAlarms;

    return (
        <section
            className={`text-text z-50 flex w-[15.5rem] origin-top transform flex-col rounded-sm border border-gray-300 bg-white p-1 text-sm leading-normal font-semibold shadow-xl transition-all duration-200 ease-out ${
                active
                    ? 'scale-y-100 opacity-100'
                    : 'pointer-events-none scale-y-0 opacity-0'
            } ${addStyle}`}
        >
            {!alarms || alarms.length === 0 ? (
                <div className="p-2">알람이 없습니다.</div>
            ) : (
                alarms.map((alarm) => (
                    <div
                        key={alarm.alarmId}
                        className="border-b border-gray-200 p-2"
                    >
                        <div className="font-bold">{alarm.title}</div>
                        <div className="text-xs">{alarm.description}</div>
                    </div>
                ))
            )}
        </section>
    );
};

export default Alarm;
