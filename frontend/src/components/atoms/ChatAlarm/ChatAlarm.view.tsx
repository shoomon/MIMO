import { Link } from 'react-router-dom';
import { Icon } from '@/components/atoms';

interface ChatAlarmViewProps {
    alarmActive: boolean;
    unreadCount: number;
}

const ChatAlarmView = ({ alarmActive, unreadCount }: ChatAlarmViewProps) => {
    return (
        <div className="relative">
            <Link to="/chat">
                <Icon type="png" id="Chat" size={44} />
            </Link>
            {alarmActive && unreadCount > 0 && (
                <div className="absolute right-0 bottom-0 z-1 flex h-6 w-6 translate-x-2 translate-y-1 items-center justify-center rounded-full bg-red-500">
                    <span className="text-md font-semibold text-white">
                        {unreadCount}
                    </span>
                </div>
            )}
        </div>
    );
};

export default ChatAlarmView;
