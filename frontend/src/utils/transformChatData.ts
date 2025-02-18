import { ChatMessageResponse } from "@/types/Chat";
import { ChatItemProps } from "@/components/atoms/ChatItem/ChatItem.view";

const transformChatData = (data: ChatMessageResponse[], nickname: string):ChatItemProps[] => {
  
    let lastUserNickname:string = "";

    if(!data) return [];

    const newChatData = data.map((item):ChatItemProps => {
      
      const type = item.nickname === nickname ? "receiver"  : "sender";

      const hasReceivedMessage = item.profileImageUri === lastUserNickname;

      if(item.chatType === "MESSAGE"){
        lastUserNickname = item.profileImageUri;
      }

      return {
        type,
        item,
        hasReceivedMessage,
      }
    })

    return newChatData;
}

export default transformChatData;