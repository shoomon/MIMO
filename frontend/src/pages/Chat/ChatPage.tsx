import ChatRoomDetail from '@/components/organisms/ChatRoomDetail/ChatRoomDetail';
import { useEffect, useState } from 'react';
import ChatRoomList from '@/components/organisms/ChatRoomList/ChatRoomList';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { ChatRoomResponse } from '@/types/Chat';
import { ChatItemProps } from '@/components/atoms/ChatItem/ChatItem.view';
import { getChatMessagesAPI } from '@/apis/ChatAPI';
import { useAuth } from '@/hooks/useAuth';
import transformChatData from '@/utils/transformChatData';

const ChatPage = () => {
    const { userInfo } = useAuth();
    const queryclient = useQueryClient();
    const [roomId, setRoomId] = useState<number>(0);
    const [roomName, setRoomName] = useState<string>('');
    const [roomImage, setRoomImage] = useState<string>('');
    const [roomData, setRoomData] = useState<ChatItemProps[]>([]);

    const handleRoomIn = (e: React.MouseEvent<HTMLButtonElement>) => {
        const currentRoomId = e.currentTarget.dataset.id;

        if (!currentRoomId) {
            return;
        }

        setRoomId(Number(currentRoomId));
    };

    const { data, isSuccess } = useQuery({
        queryKey: ['chatData', roomId],
        queryFn: () =>
            getChatMessagesAPI({
                chatroomId: roomId,
                messageId: -1,
                timestamp: 0,
            }),
        enabled: !!roomId,
        staleTime: 1000 * 10,
    });

    // const realtimeMessages = useSocketStore(
    //     (state) => state.subscriptions[roomId]?.messages || [],
    // );

    // console.log(realtimeMessages);
    // // const messages = useMemo(() => {
    // //     console.log(realtimeMessages);
    // // }, []);

    useEffect(() => {
        console.log('유저 정보', userInfo);

        if (!userInfo) return;

        const chatListData = queryclient.getQueryData<ChatRoomResponse[]>([
            'chatRoomList',
        ]);

        if (!chatListData) return;

        const currentRoom = chatListData.find((data) => {
            return data.chatroomId == String(roomId);
        });

        if (!currentRoom) {
            return;
        }

        setRoomName(currentRoom.chatroomName);
        setRoomImage(currentRoom.chatroomImage);

        if (isSuccess) {
            if (!data) return;

            console.log('채팅방 데이터', data);

            setRoomData(transformChatData(data, userInfo.profileUri));
        }
    }, [roomId, isSuccess]);

    return (
        <section className="flex h-[calc(100vh-14rem)]">
            <ChatRoomList onClick={handleRoomIn} />
            <ChatRoomDetail
                chatroomId={roomId}
                chatroomName={roomName}
                chatroomImage={roomImage}
                chatData={roomData}
            />
        </section>
    );
};

export default ChatPage;
