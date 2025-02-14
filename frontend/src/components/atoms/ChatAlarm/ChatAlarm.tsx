import { getChatListAPI } from '@/apis/ChatAPI';
import ChatAlarmView from './ChatAlarm.view';
import { useQuery } from '@tanstack/react-query';
import { useEffect } from 'react';

const ChatAlarm = () => {
    const { isSuccess } = useQuery({
        queryKey: ['userInfo'],
    });

    const { data } = useQuery({
        queryKey: ['chatRoomList'],
        queryFn: getChatListAPI,
        refetchInterval: 1000 * 10,
        enabled: isSuccess,
    });

    useEffect(() => {
        console.log(data);
    }, [data]);

    return <ChatAlarmView unreadCount={4} alarmActive={true} />;
};

export default ChatAlarm;
