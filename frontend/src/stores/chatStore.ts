import { create } from "zustand";
import { devtools } from "zustand/middleware";

export interface ChatStateType {
  isChatOpen: boolean;
  setChatOpen: () => void;
}

export const useChatStore = create<ChatStateType>()(

  devtools((set) => ({
    isChatOpen: true,
    setChatOpen: (isChatOpen: boolean) => set({isChatOpen}),
  }))
)