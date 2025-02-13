export interface ChatRoomResponse{
  chatroomId: number;
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
  chatType: "MESSAGE";
}