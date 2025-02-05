import { Link } from 'react-router-dom';
import ProfileImage, { ProfileImageProps } from '../ProfileImage/ProfileImage';

export interface MyInfoDropDownProps {
    userInfo: ProfileImageProps;
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const MyInfoDropDownView = ({ userInfo, onClick }: MyInfoDropDownProps) => {
    return (
        <section className="text-text flex w-[15.5rem] flex-col rounded-sm border-0 p-1 text-sm leading-normal font-semibold">
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
