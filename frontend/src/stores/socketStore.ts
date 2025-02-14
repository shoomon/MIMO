import { create } from "zustand";
import { Client, IMessage, Stomp } from "@stomp/stompjs";
import { ChatItemProps } from "@/components/atoms/ChatItem/ChatItem.view";
import { useTokenStore } from "./tokenStore";
import SockJS from "sockjs-client";


interface StompStore {

  // Message 상태 
  client: Client | null;
  isConnected: boolean;
  message: ChatItemProps[];

  // websocket 액션
  connect: () => void;
  disconnect: () => void;
  subscribe: () => void;
  unsubscribe: () => void;
  sendMessage: () => void;
  addMessage: () => void;
}



export const useStompStore = create<StompStore>((set, get) => ({
  client: null,
  isConnected: false,
  message: [],

  connect: () => {
    const socket = new SockJS("/ws");
    const client = Stomp.over(socket);

    const { accessToken } = useTokenStore.getState();
    const header = {Authorization: `Bearer ${accessToken}`}

    client.connect(header, (frame) => {
      console.log("Connected: " + frame);

      
    })


  },

  disconnect: () => {},
  subscribe: () => {},
  unsubscribe: () => {},
  sendMessage: () => {},
  addMessage: () => {},
}))