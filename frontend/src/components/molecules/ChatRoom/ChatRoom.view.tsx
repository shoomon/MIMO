import { Link } from 'react-router-dom';

export interface ChatRoomProps {
    id: number;
    imgSrc: string;
    title: string;
    message: string;
    date: string;
    noReadCount: number;
}

const ChatRoomView = ({
    id,
    imgSrc,
    title,
    message,
    date,
    noReadCount,
}: ChatRoomProps) => {
    return (
        <Link
            to={`/${id}`}
            className="hover:border-brand-primary-100 hover:bg-brand-primary-50 flex w-[28.5rem] gap-2 rounded-lg border-0 p-4 hover:border-2"
        >
            <img
                src={imgSrc}
                alt=""
                className="h-[4.5rem] w-[4.5rem] rounded-xl"
            />
            <div className="flex w-[19rem] flex-col">
                <span className="text-text text-lg font-extrabold">
                    {title}
                </span>
                <span className="text-text font-normal">{message}</span>
                <span className="text-sm font-normal text-gray-600">
                    {date}
                </span>
            </div>
            <div className="bg-brand-primary-400 mt-auto flex h-9 w-9 items-center justify-center rounded-full">
                <span className="text-white">{noReadCount}</span>
            </div>
        </Link>
    );
};

export default ChatRoomView;
