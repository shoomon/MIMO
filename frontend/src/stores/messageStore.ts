import { ChatMessageResponse } from "@/types/Chat";
import { create } from "zustand";

interface MessageStore {

  messages: {
    [chatroomId: string] : ChatMessageResponse[]
  };
  addMessage: (chatroomId:string, message:ChatMessageResponse) => void;
  clearMessages: (chatroomId: number) => void;

}

const useMessageStore = create<MessageStore>((set) => ({
  messages: {},
  addMessage: (chatroomId, message) => {
    set((state) => ({
      messages: {
        ...state.messages,
        [chatroomId]: [...state.messages[chatroomId], message]
      }
    }))
  },
  clearMessages: (chatroomId) => {
    set((state) => {
      const newMessages = {...state.messages};
      delete newMessages[chatroomId];
      return {messages: newMessages};
    })
  }
}));

export default useMessageStore;