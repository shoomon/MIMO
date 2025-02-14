import { ChatRoomResponse } from '@/types/Chat';
import { customFetch } from './customFetch';

export const getChatListAPI = async (): Promise<ChatRoomResponse> => {
    try {
        const response = await customFetch('/chatroom', {
            method: 'GET',
            credentials: 'include',
        });

        if (!response.ok) {
            throw new Error('데이터 요청 실패');
        }

        return response.json();
    } catch (e: unknown) {
        if (e instanceof Error) {
            console.log(e.message);
        }

        throw new Error('채팅방 요청 실패');
    }
};
