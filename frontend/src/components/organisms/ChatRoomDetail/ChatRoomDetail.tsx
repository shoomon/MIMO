
import ChatRoomDetailView, { ChatRoomDetailProps } from './ChatRoomDetail.view';


const ChatRoomDetail = ({chatroomId, chatroomName, chatroomImage, chatData}: ChatRoomDetailProps) => {
    
    
    return <ChatRoomDetailView chatroomId={chatroomId} chatroomName={chatroomName} chatroomImage={chatroomImage} chatData={chatData} />;
};

export default ChatRoomDetail;
