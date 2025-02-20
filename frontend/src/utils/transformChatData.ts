import { ChatMessageResponse } from "@/types/Chat";
import { ChatItemProps } from "@/components/atoms/ChatItem/ChatItem.view";

const transformChatData = (data: ChatMessageResponse[], nickname: string):ChatItemProps[] => {
  
    let lastUserNickname:string = "";

    

    if(!data) return [];

    const newChatData = data.map((item):ChatItemProps => {
      
      const type = item.nickname === nickname ? "receiver"  : "sender";

      const hasReceivedMessage = item.nickname === lastUserNickname;

      if(item.chatType === "MESSAGE"){
        lastUserNickname = item.profileImageUri;
      }

      return {
        type,
        item,
        hasReceivedMessage,
      }
    })
    
    console.log("현재 내 닉네임입니다.", nickname);
    console.log("가공된 데이터입니다.", newChatData);

    return newChatData;
}

export default transformChatData;