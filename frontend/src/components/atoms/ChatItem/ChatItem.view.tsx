import { ProfileImage } from '@/components/atoms';
import { ChatMessageResponse } from '@/types/Chat';

export interface ChatItemProps {
    type: 'sender' | 'receiver';
    item: ChatMessageResponse;
    hasReceivedMessage: boolean;
}

const ChatItemView = ({ type, item, hasReceivedMessage }: ChatItemProps) => {
    const STYLE =
        type === 'sender'
            ? 'text-black bg-gray-200'
            : 'text-white bg-brand-primary-400';

    return (
        <li className={`flex list-none gap-4`}>
            {type === 'sender' && !hasReceivedMessage && (
                <ProfileImage
                    nickname={item.nickname}
                    profileUri={item.profileImageUri}
                />
            )}
            <div
                className={`flex flex-col gap-1 font-bold ${type === 'receiver' ? 'ml-auto' : null}`}
            >
                {type === 'sender' && !hasReceivedMessage && (
                    <span>{item.nickname}</span>
                )}
                <span
                    className={`leading-normal font-medium ${STYLE} rounded-xl px-4 py-2 ${
                        type === 'sender' && hasReceivedMessage && 'ml-14'
                    }`}
                >
                    {item.chat}
                </span>
            </div>
        </li>
    );
};

export default ChatItemView;
