import { useState } from 'react';
import ChatInputView from './ChatInput.view';
import useSocketStore from '@/stores/socketStore';

const ChatInput = ({ chatroomId }: { chatroomId: number }) => {
    const [message, setMessage] = useState<string>('');
    const { sendMessage } = useSocketStore();

    const handleMessage = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setMessage(e.target.value);
    };

    const submitMessage = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        console.log('메세지 전송!', message, chatroomId);
        try {
            sendMessage(chatroomId, message);
        } catch (e) {
            console.error(e);
        }
    };

    return (
        <ChatInputView
            value={message}
            onChange={handleMessage}
            onSubmit={submitMessage}
        />
    );
};

export default ChatInput;
