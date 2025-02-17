export interface ChatRoomResponse{
  chatroomId: string;
  chatroomImage: string;
  chatroomName: string;
  lastChat: string;
  lastDateTime: string;
  unreadCount: number;
}

export interface ChatMessageResponse{
  id: string;
  nickname: string;
  profileImageUri: string;
  chat: string;
  timestamp: string;
  chatType: "MESSAGE" | "ENTER" | "LEAVE";
}

export interface ChatMessageRequest{
  chatroomId: number;
  messageId: number;
  timestamp: number;
}

export interface LastReadChatRequest{
  lastReadDateTime: string;
  lastReadChatId: string;
  chatroomId: number;
}