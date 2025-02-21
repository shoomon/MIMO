import { ChatRoomResponse } from '@/types/Chat';

export interface ChatRoomViewProps {
    item: ChatRoomResponse;
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const ChatRoomView = ({ item, onClick }: ChatRoomViewProps) => {
    return (
        <button
            onClick={onClick}
            data-id={item.chatroomId}
            className="hover:border-brand-primary-100 hover:bg-brand-primary-50 relative flex w-[28.5rem] gap-2 rounded-lg border-0 p-4 pr-[3.5rem] hover:border-2"
        >
            <img
                src={item.chatroomImage}
                alt=""
                className="h-[4.5rem] w-[4.5rem] rounded-xl"
            />
            <div className="max-w-[19 rem] flex flex-col justify-between overflow-hidden pr-6 text-left whitespace-nowrap">
                <span className="text-text text-lg font-extrabold">
                    {item.chatroomName}
                </span>
                <span className="text-text font-normal">{item.lastChat}</span>
                <span className="text-sm font-normal text-gray-600">
                    {item.lastDateTime}
                </span>
            </div>
            {item.unreadCount > 0 ? (
                <div className="bg-brand-primary-400 absolute right-4 bottom-7 mt-auto flex h-9 w-9 items-center justify-center rounded-full">
                    <span className="text-white">{item.unreadCount}</span>
                </div>
            ) : null}
        </button>
    );
};

export default ChatRoomView;
