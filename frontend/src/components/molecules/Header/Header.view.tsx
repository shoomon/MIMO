import { Icon, Logo } from '@/components/atoms';
import { Link } from 'react-router-dom';
import SearchBar from '../SearchBar/SearchBar';
import { ProfileImageProps } from '@/components/atoms/ProfileImage/ProfileImage';
import MyInfoDropDown from '@/components/atoms/MyInfoDropDown/MyInfoDropDown';
import { useAuth } from '@/hooks/useAuth';

export interface HeaderViewProps {
    userInfo?: ProfileImageProps;
    isLogin: boolean;
    alarmActive: boolean;
    infoActive: boolean;
    handleSearch: (e: React.FormEvent<HTMLFormElement>) => void;
    searchValue: string;
    onChangeSearch: (e: React.ChangeEvent<HTMLInputElement>) => void;
    relatedItem: string[];
    onClickSearch: (e: React.MouseEvent<HTMLButtonElement>) => void;
    onClickAlarm: (e: React.MouseEvent<HTMLButtonElement>) => void;
    onClickInfo: (e: React.MouseEvent<HTMLButtonElement>) => void;
    handleLogin: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const NoLoginedMenu = ({
    handleLogin,
}: {
    handleLogin: (e: React.MouseEvent<HTMLButtonElement>) => void;
}) => {
    return (
        <button onClick={handleLogin} className="cursor-pointer">
            <span>로그인</span>
            <span>/</span>
            <span>회원가입</span>
        </button>
    );
};

const LoginedMenu = ({
    userInfo,
    alarmActive,
    infoActive,
    onClickAlarm,
    onClickInfo,
}: {
    userInfo?: ProfileImageProps;
    alarmActive: boolean;
    infoActive: boolean;
    onClickAlarm: (e: React.MouseEvent<HTMLButtonElement>) => void;
    onClickInfo: (e: React.MouseEvent<HTMLButtonElement>) => void;
}) => {
    return (
        <div className="flex gap-4">
            <Link to="/">
                <Icon type="png" id="Alarm" size={44} />
            </Link>
            <div>
                <button onClick={onClickAlarm} className="cursor-pointer">
                    <Icon type="png" id="Chat" size={44} />
                </button>
                {/* {해당 부분에 알림 dropdown 필요} */}
                {alarmActive}
            </div>
            <div className="relative">
                <button onClick={onClickInfo} className="cursor-pointer">
                    <Icon type="png" id="User" size={44} />
                </button>
                <MyInfoDropDown
                    active={infoActive}
                    userInfo={userInfo}
                    addStyle="absolute right-0 translate-y-full -bottom-4"
                />
            </div>
        </div>
    );
};

const HeaderView = ({
    isLogin,
    alarmActive,
    infoActive,
    handleSearch,
    searchValue,
    onChangeSearch,
    relatedItem,
    onClickSearch,
    onClickAlarm,
    onClickInfo,
    handleLogin,
}: HeaderViewProps) => {
    // 유저 정보 받아와서 렌더링해야함함
    const { userInfo } = useAuth();

    return (
        <header className="w-full py-5">
            <div className="flex flex-wrap items-center justify-between gap-4">
                <h1 className="">
                    <Link to="/">
                        <Logo />
                    </Link>
                </h1>

                <div className="order-1 w-full lg:order-none lg:mx-8 lg:w-auto lg:max-w-xl lg:flex-1">
                    <SearchBar
                        handleSubmit={handleSearch}
                        value={searchValue}
                        onChange={onChangeSearch}
                        relatedItem={relatedItem}
                        onClick={onClickSearch}
                    />
                </div>

                {isLogin === true ? (
                    <LoginedMenu
                        onClickAlarm={onClickAlarm}
                        onClickInfo={onClickInfo}
                        userInfo={userInfo}
                        alarmActive={alarmActive}
                        infoActive={infoActive}
                    />
                ) : (
                    <NoLoginedMenu handleLogin={handleLogin} />
                )}
            </div>
        </header>
    );
};

export default HeaderView;
