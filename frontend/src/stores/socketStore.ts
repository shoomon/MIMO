import { Client, IMessage, Stomp, StompSubscription } from "@stomp/stompjs";
import SockJS from "sockjs-client/dist/sockjs";
import { create } from "zustand";
import { useTokenStore } from "./tokenStore";


interface ChatMessage {
  message: string;
}
interface Subscription {
  subscription: StompSubscription;
  messages: ChatMessage[];
}

interface SocketStore{
  client: Client | null;
  subscriptions: {
    [chatroomId: string]: Subscription
  };
  connectionStatus: "connected" | "disconnected" | "error";
  connect: () => void;
  subscribeRoom: (chatroomId: string) => void;
  unsubscribeRoom: (chatroomId: string) => void;
  sendMessage: (chatroomId: number, message: string) => void;
  disconnect: () => void;
}



const useSocketStore = create<SocketStore>((set, get) => ({
  client: null,
  subscriptions: {},
  connectionStatus: "disconnected",
  connect: () => {
    const { client } = get();
    if(client) return;

    const accessToken = useTokenStore.getState().accessToken;

    if(!accessToken){
      return;
    }

    const socket = new SockJS(`${import.meta.env.VITE_BASE_URL}/ws?token=${accessToken}`);

    const newClient = Stomp.over(socket);
    
    const headers = {Authorization: `${accessToken}` }

    newClient.connect(headers, (frame:string) => {
      console.log("connected", frame);
      set({client: newClient, connectionStatus:"connected" as const})
    })
  },
  subscribeRoom: (chatroomId: string) => {
    const { client, subscriptions } = get();
    
    if(!client){
      return;
    }

    if(subscriptions[chatroomId]){
      return;
    }

    const subscription = client.subscribe(`/sub/chat/${chatroomId}`, (message: IMessage)=>{
        const messageData = JSON.parse(message.body);

        console.log("소켓 데이터",message);

        set((state) => ({
          ...state,
          subscriptions: {
            ...state.subscriptions,
            [chatroomId]: {
              subscription,
              messages: [...(state.subscriptions[chatroomId]?.messages || []), messageData]
            }
          }
        }))
    });

    set((state) => ({
      subscriptions: {
        ...state.subscriptions,
        [chatroomId]: {
          subscription,
          messages:[]
        }
      }
    }))

  },
  unsubscribeRoom: (chatroomId: string) => {
    const { subscriptions } = get();
    const currentSubscription = subscriptions[chatroomId];

    if(!currentSubscription) return;

    currentSubscription.subscription.unsubscribe();

    set((state) => {
      const newSubscriptions = {...state.subscriptions};
      delete newSubscriptions[chatroomId];

      return {...state, subscriptions:newSubscriptions}
    })
  },
  sendMessage: (chatroomId: number, message: string) => {
    const {client} = get();
    const { accessToken } = useTokenStore.getState();

    if(!client) return;
    if(!client.connected) return;
    if(!accessToken) return;

    const header = { Authorization : `Bearer ${accessToken}`};
    
    client.publish({
      destination: `/pub/chat-message/${chatroomId}`,
      headers: header,
      body: JSON.stringify({message})
    })
  },
  disconnect: () => {
    const {client} = get();
    
    if(!client) return;

    client.deactivate();

    set({client: null, connectionStatus: "disconnected" as const });
  }
}));

export default useSocketStore;