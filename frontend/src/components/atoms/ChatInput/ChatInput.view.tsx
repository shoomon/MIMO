import Icon from '../Icon/Icon';

interface ChatInputViewProps {
    value: string;
    onChange: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
    onSubmit: (e: React.FormEvent<HTMLFormElement>) => void;
    onKeyDown: (e: React.KeyboardEvent<HTMLTextAreaElement>) => void;
}

const ChatInputView = ({ value, onChange, onSubmit, onKeyDown }: ChatInputViewProps) => {
    return (
        <form onSubmit={onSubmit} className="relative mt-auto p-6">
            <label htmlFor="chatInput"></label>
            <textarea
                placeholder="메시지 입력"
                value={value}
                onChange={onChange}
                onKeyDown={onKeyDown}
                id="chatInput"
                name="chatInput"
                className="w-full resize-none rounded border border-gray-300 py-3 pr-[3.25rem] pl-4"
            />
            <button
                type="submit"
                className="absolute top-[3.125rem] right-10 cursor-pointer"
            >
                <Icon type="svg" id="SendBlue" />
            </button>
        </form>
    );
};

export default ChatInputView;
