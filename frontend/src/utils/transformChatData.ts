import { ChatMessageResponse } from "@/types/Chat";
import { ChatItemProps } from "@/components/atoms/ChatItem/ChatItem.view";

const transformChatData = (data: ChatMessageResponse[], userImageUri: string):ChatItemProps[] => {
  
    let lastUserImageUri:string = "";

    const newChatData = data.map((item):ChatItemProps => {
      
      const type = item.profileImageUri === userImageUri ? "receiver"  : "sender";

      const hasReceivedMessage = item.profileImageUri === lastUserImageUri;

      if(item.chatType === "MESSAGE"){
        lastUserImageUri = item.profileImageUri;
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