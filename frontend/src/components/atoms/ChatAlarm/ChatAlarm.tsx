import { getChatListAPI } from '@/apis/ChatAPI';
import ChatAlarmView from './ChatAlarm.view';
import { useQuery } from '@tanstack/react-query';
import { useEffect } from 'react';
import { ChatRoomResponse } from '@/types/Chat';

const ChatAlarm = () => {
    const { isSuccess } = useQuery({
        queryKey: ['userInfo'],
    });

    const { data } = useQuery<ChatRoomResponse[]>({
        queryKey: ['chatRoomList'],
        queryFn: getChatListAPI,
        refetchInterval: 1000 * 10,
        enabled: isSuccess,
    });

    useEffect(() => {
        console.log(data);
        // unreadCount 만들어지면 연결 필요
        // unreadCount 개수로 alarmActive도 설정
    }, [data]);

    return <ChatAlarmView unreadCount={4} alarmActive={true} />;
};

export default ChatAlarm;
