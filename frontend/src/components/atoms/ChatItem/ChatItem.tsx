import ChatItemView, { ChatItemProps } from './ChatItem.view';

/**
 * 채팅 메세지에 대한 ChatItem 컴포넌트입니다.
 * @component
 * @param {('sender' | 'receiver')} type  - sender, recevier를 입력받아서 수신자인지 발신자인지 표시합니다.
 * @param {string} message  - 표시될 메세지 입니다.
 * @returns {JSX.Element} - 렌더링된 ChatIem 컴포넌트를 반환합니다.
 */
const ChatItem = ({ type, item, hasReceivedMessage }: ChatItemProps) => {
    const props = { type, item, hasReceivedMessage };

    return <ChatItemView {...props} />;
};

export default ChatItem;
