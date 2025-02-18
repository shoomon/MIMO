import { useState } from 'react';
import ChatInputView from './ChatInput.view';
import useSocketStore from '@/stores/socketStore';

const ChatInput = ({
    chatroomId,
    onMessageSent,
}: {
    chatroomId: number;
    onMessageSent: () => void;
}) => {
    const [message, setMessage] = useState<string>('');
    const { sendMessage } = useSocketStore();

    const handleMessage = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setMessage(e.target.value);
    };

    const sendMessageHandler = () => {
        if (message.trim() == '') return;

        console.log('메세지 전송!', message, chatroomId);
        try {
            sendMessage(chatroomId, message);
            setMessage('');
            onMessageSent();
        } catch (e) {
            console.error(e);
        }
    };

    const submitMessage = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        sendMessageHandler();
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
        if (e.key == 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendMessageHandler();
        }
    };

    return (
        <ChatInputView
            value={message}
            onChange={handleMessage}
            onSubmit={submitMessage}
            onKeyDown={handleKeyDown}
        />
    );
};

export default ChatInput;
