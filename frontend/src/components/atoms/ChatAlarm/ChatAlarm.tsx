import { getChatListAPI } from '@/apis/ChatAPI';
import ChatAlarmView from './ChatAlarm.view';
import { useQuery } from '@tanstack/react-query';
import { useEffect, useState } from 'react';
import { ChatRoomResponse } from '@/types/Chat';
import useSocketStore from '@/stores/socketStore';

const ChatAlarm = () => {
    const { isSuccess } = useQuery({
        queryKey: ['userInfo'],
    });

    const { subscribeRoom } = useSocketStore();
    const [unreadCount, setUnreadCount] = useState<number>(0);

    const { data } = useQuery<ChatRoomResponse[]>({
        queryKey: ['chatRoomList'],
        queryFn: getChatListAPI,
        staleTime: 1000 * 15,
        enabled: isSuccess,
    });

    useEffect(() => {
        if (!data) return;
        console.log('채팅방 기록', data);
        // unreadCount 만들어지면 연결 필요
        // unreadCount 개수로 alarmActive도 설정
        let count = 0;
        data.forEach((item) => {
            subscribeRoom(item.chatroomId);
            count += item.unreadCount;
        });

        setUnreadCount(count);
    }, [data]);

    return <ChatAlarmView unreadCount={unreadCount} alarmActive={true} />;
};

export default ChatAlarm;
