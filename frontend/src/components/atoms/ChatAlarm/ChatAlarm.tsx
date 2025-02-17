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

    const [unreadCount, setUnreadCount] = useState<number>(0);
    const subscribeRoom = useSocketStore((state) => state.subscribeRoom);

    const { data } = useQuery<ChatRoomResponse[]>({
        queryKey: ['chatRoomList'],
        queryFn: getChatListAPI,
        refetchInterval: 1000 * 10,
        enabled: isSuccess,
    });

    useEffect(() => {
        if (!data) return;

        let count = 0;
        data.forEach((item) => {

            count += item.unreadCount;
            subscribeRoom(item.chatroomId);
        });

        setUnreadCount(count);
    }, [data]);

    return <ChatAlarmView unreadCount={unreadCount} alarmActive={true} />;
};

export default ChatAlarm;
