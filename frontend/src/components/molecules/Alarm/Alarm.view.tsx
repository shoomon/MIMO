// src/components/atoms/AlarmView.tsx
import React, { useState } from 'react';
import { Icon } from '@/components/atoms';
import Alarm from './Alarm';
import { UserAlarmsResponse } from '@/types/User';
import { useQuery } from '@tanstack/react-query';
import { useAuth } from '@/hooks/useAuth';
import { getMyAlarm } from '@/apis/UserAPI';

const AlarmView = () => {
    const { userInfo } = useAuth();

    const { data } = useQuery<UserAlarmsResponse>({
        queryKey: ['userAlarm', userInfo?.nickname],
        queryFn: () => getMyAlarm(),
        enabled: !!userInfo,
    });

    // 미확인 알람 개수를 data에서 받아옴 (데이터 없으면 0)
    const unreadCount = data?.userAlarms?.length ?? 0;

    const [alarmActive, setAlarmActive] = useState(false);
    // 여기서 userInfoActive는 헤더의 다른 드롭다운(내 정보)와 겹치지 않도록 상위에서 관리할 수도 있음

    const handleAlarmClick = () => {
        setAlarmActive((prev) => !prev);
    };

    // 알람 데이터가 없으면 아이콘만 표시할 수도 있음
    return (
        <div className="relative">
            <div>
                <button onClick={handleAlarmClick} className="cursor-pointer">
                    <Icon type="png" id="Alarm" size={44} />
                </button>
                {alarmActive && unreadCount > 0 && (
                    <div className="absolute right-0 bottom-0 z-10 flex h-6 w-6 translate-x-2 translate-y-1 items-center justify-center rounded-full bg-red-500">
                        <span className="text-md font-semibold text-white">
                            {unreadCount}
                        </span>
                    </div>
                )}
            </div>
            {data && (
                <Alarm
                    active={alarmActive}
                    setActive={setAlarmActive}
                    addStyle="absolute right-0 translate-y-full -bottom-4"
                    data={data}
                />
            )}
        </div>
    );
};

export default AlarmView;
