export interface ChatItemProps {
    type: 'sender' | 'receiver';
    message: string;
}

const ChatItemView = ({ type, message }: ChatItemProps) => {
    const BG_COLOR = type === 'sender' ? 'bg-gray-200' : 'bg-brand-primary-400';
    const TEXT_COLOR = type === 'sender' ? 'text-black' : 'text-white';

    return (
        <li className={`px-4 py-2 ${BG_COLOR} list-none rounded-xl`}>
            <span className={`leading-normal font-medium ${TEXT_COLOR}`}>
                {message}
            </span>
        </li>
    );
};

export default ChatItemView;
