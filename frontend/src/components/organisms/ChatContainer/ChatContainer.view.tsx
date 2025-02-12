import { ChatRoom } from '@/components/molecules';
import ChatRoomDetail from '@/components/molecules/ChatRoomDetail/ChatRoomDetail';
import { ChatRoomResponse } from '@/types/Chat';

interface ChatContainerViewProps {
    chatListItems: ChatRoomResponse[];
    chatroomName: string | null;
    chatroomImage: string | null;
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const ChatContainerView = ({
    chatListItems,
    chatroomName,
    chatroomImage,
    onClick,
}: ChatContainerViewProps) => {
    return (
        <section className="flex h-[64rem] w-full bg-lime-100">
            <section className="flex w-[30.5rem] flex-col border border-gray-200">
                <span className="p-6 text-center text-2xl font-bold">
                    채팅방
                </span>
                <section className="flex flex-col items-center border-t border-gray-200">
                    {chatListItems.map((item) => {
                        return (
                            <ChatRoom
                                item={item}
                                key={item.chatroomId}
                                onClick={onClick}
                            />
                        );
                    })}
                </section>
            </section>
            {chatroomName && chatroomImage && (
                <ChatRoomDetail
                    chatroomName={chatroomName}
                    chatroomImage={chatroomImage}
                />
            )}
        </section>
    );
};

export default ChatContainerView;
