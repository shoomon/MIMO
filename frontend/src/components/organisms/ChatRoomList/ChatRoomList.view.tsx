import { ChatRoom } from '@/components/molecules';
import { ChatRoomResponse } from '@/types/Chat';

export interface ChatRoomListViewProps {
    chatListItems: ChatRoomResponse[];
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const ChatRoomListView = ({
    chatListItems,
    onClick,
}: ChatRoomListViewProps) => {
    return (
        <section className="flex w-full max-w-[30.5rem] flex-col border border-gray-200">
            <span className="p-6 text-center text-2xl font-bold">채팅방</span>
            <section className="flex flex-1 flex-col items-center overflow-y-scroll border-t border-gray-200">
                {chatListItems.map((item: ChatRoomResponse) => {
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
    );
};

export default ChatRoomListView;
