import { ChatRoomResponse } from '@/types/Chat';
import ChatRoomListView from './ChatRoomList.view';
import { useQuery } from '@tanstack/react-query';

interface ChatRoomListProps {
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const ChatRoomList = ({ onClick }: ChatRoomListProps) => {
    const { data: chatListData, isSuccess } = useQuery<ChatRoomResponse[]>({
        queryKey: ['chatRoomList'],
    });

    if (!isSuccess) {
        return (
            <section className="flex w-full max-w-[30.5rem] flex-col border border-gray-200">
                <span className="p-6 text-center text-2xl font-bold">
                    채팅방
                </span>
                <section className="flex flex-1 flex-col items-center overflow-y-scroll border-t border-gray-200"></section>
            </section>
        );
    }

    return <ChatRoomListView onClick={onClick} chatListItems={chatListData} />;
};

export default ChatRoomList;
