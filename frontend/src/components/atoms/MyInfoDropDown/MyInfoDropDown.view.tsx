import { Link } from 'react-router-dom';
import ProfileImage, { ProfileImageProps } from '../ProfileImage/ProfileImage';

export interface MyInfoDropDownViewProps {
    userInfo: ProfileImageProps;
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
    active: boolean;
    addStyle: string;
}

const MyInfoDropDownView = ({
    userInfo,
    onClick,
    active,
    addStyle,
}: MyInfoDropDownViewProps) => {
    return (
        <section
            className={`text-text flex w-[15.5rem] origin-top transform flex-col rounded-sm p-1 text-sm leading-normal font-semibold transition-all duration-200 ease-out ${
                active
                    ? 'scale-y-100 opacity-100'
                    : 'pointer-events-none scale-y-0 opacity-0'
            } ${addStyle} `}
        >
            <div className="flex gap-2 border-b border-gray-300 px-3 py-2 hover:bg-gray-100">
                <ProfileImage {...userInfo} size={20} />
                <span className="">{userInfo.userName}</span>
            </div>
            <Link
                to={`/${userInfo.userId}`}
                className="px-2 py-1.5 hover:bg-gray-100"
            >
                마이페이지
            </Link>
            <button
                onClick={onClick}
                className="px-2 py-1.5 text-left hover:bg-gray-100"
            >
                로그아웃
            </button>
        </section>
    );
};

export default MyInfoDropDownView;
