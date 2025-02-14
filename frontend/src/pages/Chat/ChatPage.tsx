import ChatRoomDetail from '@/components/organisms/ChatRoomDetail/ChatRoomDetail';
import { CHAT_DATA, CHAT_LIST_DATA } from '@/mock/ChatServiceMock';
import { useEffect, useState } from 'react';
import ChatRoomList from '@/components/organisms/ChatRoomList/ChatRoomList';

const ChatPage = () => {
    const [roomId, setRoomId] = useState<number>(0);
    const [roomName, setRoomName] = useState<string>('');
    const [roomImage, setRoomImage] = useState<string>('');

    const handleRoomIn = (e: React.MouseEvent<HTMLButtonElement>) => {
        const currentRoomId = e.currentTarget.dataset.id;

        if (!currentRoomId) {
            return;
        }

        setRoomId(Number(currentRoomId));
    };

    useEffect(() => {
        const currentRoom = CHAT_LIST_DATA.find((data) => {
            return data.chatroomId === roomId;
        });

        if (!currentRoom) {
            return;
        }

        setRoomName(currentRoom.chatroomName);
        setRoomImage(currentRoom.chatroomImage);
    }, [roomId]);

    return (
        <section className="flex h-[calc(100vh-14rem)]">
            <ChatRoomList
                onClick={handleRoomIn}
                chatListItems={CHAT_LIST_DATA}
            />
            <ChatRoomDetail
                chatroomName={roomName}
                chatroomImage={roomImage}
                chatData={CHAT_DATA}
            />
        </section>
    );
};

export default ChatPage;
