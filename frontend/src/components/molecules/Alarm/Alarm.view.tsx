// src/components/atoms/AlarmView.tsx
import React, { useState, useRef, useEffect } from 'react';
import { Icon } from '@/components/atoms';
import Alarm from './Alarm';
import { UserAlarmsResponse } from '@/types/User';
import { useQuery } from '@tanstack/react-query';
import { useAuth } from '@/hooks/useAuth';
import { getMyAlarm } from '@/apis/UserAPI';
import { useLocation } from 'react-router-dom';

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

    const handleAlarmClick = (e: React.MouseEvent) => {
        e.stopPropagation();
        setAlarmActive((prev) => !prev);
    };

    // 알람창 영역을 감싸는 ref
    const alarmRef = useRef<HTMLDivElement>(null);

    // ✅ 다른 곳 클릭 시 알람창 닫기
    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (
                alarmRef.current &&
                !alarmRef.current.contains(event.target as Node)
            ) {
                setAlarmActive(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () =>
            document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    // ✅ 페이지 이동 시 알람창 닫기
    const location = useLocation();
    useEffect(() => {
        setAlarmActive(false);
    }, [location]);

    return (
        <div className="relative" ref={alarmRef}>
            <div>
                <button onClick={handleAlarmClick} className="cursor-pointer">
                    <Icon type="png" id="Alarm" size={44} />
                </button>
                {unreadCount > 0 && (
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
