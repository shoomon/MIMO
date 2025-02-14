import { Icon } from '@/components/atoms';
import ChatItemView, {
    ChatItemProps,
} from '@/components/atoms/ChatItem/ChatItem.view';
import { Link } from 'react-router-dom';

export interface ChatRoomDetailProps {
    chatroomName: string;
    chatroomImage: string;
    chatData: ChatItemProps[];
}

const ChatRoomDetailView = ({
    chatroomName,
    chatroomImage,
    chatData,
}: ChatRoomDetailProps) => {
    return (
        <section className="flex w-full flex-col border border-gray-200 bg-white">
            <div className="flex justify-between p-6">
                <div className="flex items-center gap-2">
                    {chatroomImage && (
                        <img
                            src={chatroomImage}
                            alt=""
                            className="h-[1.875rem] w-[1.875rem] rounded-sm"
                        />
                    )}
                    <span className="text-2xl font-bold">{chatroomName}</span>
                </div>
                <Link
                    to="/영상통화"
                    className="border-dark flex items-center gap-2 rounded-sm border px-2 py-1 hover:bg-gray-100"
                >
                    <Icon type="svg" id="VideoCall" size={20} />
                    <span>영상 통화</span>
                </Link>
            </div>
            <div className="flex h-full w-full flex-col gap-2.5 overflow-y-scroll border-t border-gray-200 p-6">
                {chatData.map((data) => {
                    return <ChatItemView {...data} key={data.item.id} />;
                })}
            </div>
            <form onSubmit={() => {}} className="relative mt-auto p-6">
                <label htmlFor="chatInput"></label>
                <textarea
                    placeholder="메시지 입력"
                    id="chatInput"
                    name="chatInput"
                    className="w-full resize-none rounded border border-gray-300 py-3 pr-[3.25rem] pl-4"
                />
                <button
                    type="submit"
                    className="absolute top-[3.125rem] right-10"
                >
                    <Icon type="svg" id="SendBlue" />
                </button>
            </form>
        </section>
    );
};

export default ChatRoomDetailView;
