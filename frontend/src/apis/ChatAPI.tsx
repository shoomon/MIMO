import {
    ChatMessageRequest,
    ChatMessageResponse,
    ChatRoomResponse,
    LastReadChatRequest,
} from '@/types/Chat';
import { customFetch } from './customFetch';

export const getChatListAPI = async (): Promise<ChatRoomResponse[]> => {
    try {
        const response = await customFetch('/chatroom', {
            method: 'GET',
            credentials: 'include',
        });

        if (!response.ok) {
            throw new Error('데이터 요청 실패');
        }

        const data = await response.json();

        console.log(data);

        return data;
    } catch (e: unknown) {
        if (e instanceof Error) {
            console.log(e.message);
        }

        throw new Error('채팅방 요청 실패');
    }
};

export const getChatMessagesAPI = async ({
    chatroomId,
    messageId,
    timestamp,
}: ChatMessageRequest): Promise<ChatMessageResponse[]> => {
    try {
        const response = await customFetch(
            `/chat-message/messages/${chatroomId}?messageId=${messageId}&timestamp=${timestamp}`,
            {
                method: 'GET',
                credentials: 'include',
            },
        );

        if (!response.ok) {
            throw new Error('데이터 요청 실패');
        }

        const data = await response.json();

        return data;
    } catch (e: unknown) {
        if (e instanceof Error) {
            console.log(e.message);
        }

        throw new Error('채팅방 메시지 가져오기 실패');
    }
};

export const lastReadChatAPI = async ({lastReadDateTime, lastReadChatId, chatroomId}:LastReadChatRequest): Promise<void> => {

    const readData = {
        lastReadDateTime,
        lastReadChatId,
        chatroomId
    }

    try{
        const response = await customFetch("/chatroom/last-read", {
            method: "POST",
            body: JSON.stringify(readData)
        });

        if(!response.ok){
            throw new Error("읽음 처리 실패");
        }

    }catch(e: unknown){
        console.error(e);
    }

}