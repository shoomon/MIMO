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
        return <div>채팅방 데이터가 없습니다..</div>;
    }

    return <ChatRoomListView onClick={onClick} chatListItems={chatListData} />;
};

export default ChatRoomList;
