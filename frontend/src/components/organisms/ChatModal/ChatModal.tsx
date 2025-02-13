import ChatRoomDetail from '@/components/molecules/ChatRoomDetail/ChatRoomDetail';
import ChatContainer from '../ChatContainer/ChatContainer';

interface ChatModalProps {
    type: 'list' | 'room';
    isChatOpen: boolean;
}

const ChatModal = ({ type, isChatOpen }: ChatModalProps) => {
    return (
        <section
            className={`fixed inset-0 z-50 flex items-center justify-center bg-gray-500/50 ${isChatOpen ? 'block' : 'hidden'}`}
        >
            {type ? (
                <ChatRoomDetail
                    chatroomName=""
                    chatroomImage=""
                    chatData={[]}
                />
            ) : (
                <ChatContainer />
            )}
        </section>
    );
};

export default ChatModal;
