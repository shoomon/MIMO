import { lastReadChatAPI } from '@/apis/ChatAPI';
import { Icon } from '@/components/atoms';
import ChatInput from '@/components/atoms/ChatInput/ChatInput';
import ChatItemView, {
    ChatItemProps,
} from '@/components/atoms/ChatItem/ChatItem.view';
import { useEffect, useRef } from 'react';
import { Link } from 'react-router-dom';

export interface ChatRoomDetailProps {
    chatroomId: number;
    chatroomName: string;
    chatroomImage: string;
    chatData: ChatItemProps[];
}

const ChatRoomDetailView = ({
    chatroomId,
    chatroomName,
    chatroomImage,
    chatData,
}: ChatRoomDetailProps) => {
    const chatContainerRef = useRef<HTMLDivElement>(null);
    const scrollToBottom = () => {
        if(chatContainerRef.current){
            chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
        }
    }
    const handleMessageSent = () => {
        setTimeout(scrollToBottom, 250);
    }

    useEffect(() => {

        if(!chatData){
            return;
        }

        console.log("채팅 데이터 가져오기", chatData[chatData.length - 1]);
        const lastData = chatData[chatData.length - 1];

        const lastRead = async () => {
            try{
                await lastReadChatAPI({lastReadChatId: lastData.item.id, chatroomId: chatroomId, lastReadDateTime:lastData.item.timestamp});
                console.log("읽기 성공");
            }catch(error){
                console.error(error);
            }
        }

        lastRead();
        handleMessageSent();

        return;
    },[chatData])

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
                    to="/video"
                    className="border-dark flex items-center gap-2 rounded-sm border px-2 py-1 hover:bg-gray-100"
                >
                    <Icon type="svg" id="VideoCall" size={20} />
                    <span>영상 통화</span>
                </Link>
            </div>
            <div ref={chatContainerRef} className="flex h-full w-full flex-col gap-2.5 overflow-y-scroll border-t border-gray-200 p-6">
                {chatData && chatData.map((data) => {
                    return <ChatItemView {...data} key={data.item.timestamp} />;
                })}
            </div>
            {chatroomName && <ChatInput chatroomId={chatroomId} onMessageSent={handleMessageSent}/>}
        </section>
    );
};

export default ChatRoomDetailView;
