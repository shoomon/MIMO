import ChatRoomView, { ChatRoomProps } from './ChatRoom.view';

const ChatRoom = ({
    id,
    imgSrc,
    title,
    message,
    date,
    noReadCount,
}: ChatRoomProps) => {
    const shortMessage =
        message.length > 20 ? message.substring(0, 20) + ' ...' : message;
    return (
        <ChatRoomView
            id={id}
            imgSrc={imgSrc}
            title={title}
            message={shortMessage}
            date={date}
            noReadCount={noReadCount}
        />
    );
};

export default ChatRoom;
