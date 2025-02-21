import ChatRoomDetail from '@/components/organisms/ChatRoomDetail/ChatRoomDetail';
import { useEffect, useState } from 'react';
import ChatRoomList from '@/components/organisms/ChatRoomList/ChatRoomList';
import { useQuery } from '@tanstack/react-query';
import { ChatRoomResponse } from '@/types/Chat';
import { ChatItemProps } from '@/components/atoms/ChatItem/ChatItem.view';
import { getChatListAPI, getChatMessagesAPI } from '@/apis/ChatAPI';
import { useAuth } from '@/hooks/useAuth';
import transformChatData from '@/utils/transformChatData';
import useSocketStore from '@/stores/socketStore';

const ChatPage = () => {
    const { userInfo } = useAuth();
    const [roomId, setRoomId] = useState<number>(0);
    const [roomName, setRoomName] = useState<string>('');
    const [roomImage, setRoomImage] = useState<string>('');
    const [roomData, setRoomData] = useState<ChatItemProps[]>([]);
    const messages = useSocketStore((state) => state.messages);
    const clearRoomMessages = useSocketStore(
        (state) => state.clearRoomMessages,
    );

    const handleRoomIn = (e: React.MouseEvent<HTMLButtonElement>) => {
        const currentRoomId = e.currentTarget.dataset.id;

        if (!currentRoomId) {
            return;
        }

        setRoomId(Number(currentRoomId));

        clearRoomMessages(currentRoomId);
    };

    const { data } = useQuery({
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

    const { data: chatListData } = useQuery<ChatRoomResponse[]>({
        queryKey: ['chatRoomList'],
        queryFn: getChatListAPI,
        staleTime: 1000 * 15,
    });

    useEffect(() => {
        if (!userInfo) return;
        if (!data) return;
        if (!chatListData) return;

        const currentRoom = chatListData.find((data) => {
            return data.chatroomId == String(roomId);
        });

        if (!currentRoom) {
            return;
        }

        setRoomName(currentRoom.chatroomName);
        setRoomImage(currentRoom.chatroomImage);
        setRoomData([
            ...transformChatData(data, currentRoom.nickname),
            ...transformChatData(messages[roomId], currentRoom.nickname),
        ]);
    }, [roomId, data, userInfo, chatListData, messages]);

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
