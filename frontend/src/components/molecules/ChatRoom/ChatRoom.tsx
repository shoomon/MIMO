import ChatRoomView, { ChatRoomViewProps } from './ChatRoom.view';

const ChatRoom = ({ item, onClick }: ChatRoomViewProps) => {
    const shortMessage =
        item.lastChat.length > 20
            ? item.lastChat.substring(0, 20) + ' ...'
            : item.lastChat;

    const chatRooms = { ...item, lastChat: shortMessage };

    return <ChatRoomView item={chatRooms} onClick={onClick} />;
};

export default ChatRoom;
