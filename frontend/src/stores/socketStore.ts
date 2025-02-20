import { Client, IMessage, Stomp, StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { create } from 'zustand';
import { useTokenStore } from './tokenStore';
import { ChatMessageResponse } from '@/types/Chat';

export interface SocketStore {
    client: Client | null;
    messages: {
        [chatroomId: string]: ChatMessageResponse[];
    };
    subscriptions: {
        [chatroomId: string]: StompSubscription;
    };
    connectionStatus: 'connected' | 'disconnected' | 'error';
    connect: (accessToken: string) => void;
    clearRoomMessages: (chatroomId: string) => void;
    subscribeRoom: (chatroomId: string) => void;
    unsubscribeRoom: (chatroomId: string) => void;
    sendMessage: (chatroomId: number, message: string) => void;
    disconnect: () => void;
}

const useSocketStore = create<SocketStore>((set, get) => ({
    client: null,
    messages: {},
    subscriptions: {},
    connectionStatus: 'disconnected',
    connect: (accessToken: string) => {
        const { client } = get();

        if (client) return;

        const socket = new SockJS(
            `${import.meta.env.VITE_BASE_URL}/ws?token=${accessToken}`,
        );

        const newClient = Stomp.over(socket);

        const headers = { Authorization: `${accessToken}` };

        newClient.connect(headers, (frame: string) => {
            console.log('connected', frame);
            set({ client: newClient, connectionStatus: 'connected' as const });
        });
    },
    subscribeRoom: (chatroomId: string) => {
        const { client, subscriptions } = get();

        if (!client || subscriptions[chatroomId]) {
            return;
        }

        const subscription = client.subscribe(
            `/sub/chat/${chatroomId}`,
            (message: IMessage) => {
                const messageData = JSON.parse(message.body);

                // 메시지 업데이트 로직 개선
                set((state) => ({
                    messages: {
                        ...state.messages,
                        [chatroomId]: [
                            ...(state.messages[chatroomId] || []),
                            messageData,
                        ],
                    },
                }));
            },
        );

        // 구독 정보만 저장
        set((state) => ({
            subscriptions: {
                ...state.subscriptions,
                [chatroomId]: subscription,
            },
        }));
    },
    clearRoomMessages: (chatroomId: string) => {
        set((state) => ({
            messages: {
                ...state.messages,
                [chatroomId]: [],
            }
        }))
    },
    unsubscribeRoom: (chatroomId: string) => {
        const { subscriptions } = get();
        const currentSubscription = subscriptions[chatroomId];

        if (!currentSubscription) return;

        currentSubscription.unsubscribe();

        set((state) => {
            const newSubscriptions = { ...state.subscriptions };
            const newMessages = { ...state.messages };
            delete newSubscriptions[chatroomId];
            delete newMessages[chatroomId];

            return { subscriptions: newSubscriptions, messages: newMessages };
        });
    },
    sendMessage: (chatroomId: number, message: string) => {
        const { client } = get();
        const { accessToken } = useTokenStore.getState();

        if (!client) return;
        if (!client.connected) return;
        if (!accessToken) return;

        const header = { Authorization: `${accessToken}` };

        client.publish({
            destination: `/pub/chat-message/${chatroomId}`,
            headers: header,
            body: JSON.stringify({ message }),
        });
    },
    disconnect: () => {
        const { client } = get();

        if (!client) return;

        client.deactivate();

        set({ client: null, connectionStatus: 'disconnected' as const });
    },
}));

export default useSocketStore;
