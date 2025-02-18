import { ChatAlarm, Icon, Logo } from '@/components/atoms';
import { Link } from 'react-router-dom';
import SearchBar from '../SearchBar/SearchBar';
import { ProfileImageProps } from '@/components/atoms/ProfileImage/ProfileImage';
import MyInfoDropDown from '@/components/atoms/MyInfoDropDown/MyInfoDropDown';
import { customFetch } from '@/apis/customFetch';
import { useTokenStore } from '@/stores/tokenStore';
import { useAuth } from '@/hooks/useAuth';

export interface HeaderViewProps {
    userInfo?: ProfileImageProps;
    isLogin: boolean;
    setLogin: React.Dispatch<React.SetStateAction<boolean>>;
    setActive: React.Dispatch<React.SetStateAction<boolean>>;
    alarmActive: boolean;
    infoActive: boolean;
    handleSearch: (e: React.FormEvent<HTMLFormElement>) => void;
    searchValue: string;
    onChangeSearch: (e: React.ChangeEvent<HTMLInputElement>) => void;
    relatedItem: string[];
    onClickSearch: (e: React.MouseEvent<HTMLButtonElement>) => void;
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
    infoActive,
    onClickInfo,
    setLogin,
    setActive,
}: {
    userInfo?: ProfileImageProps;
    alarmActive: boolean;
    infoActive: boolean;
    onClickInfo: (e: React.MouseEvent<HTMLButtonElement>) => void;
    setLogin: React.Dispatch<React.SetStateAction<boolean>>;
    setActive: React.Dispatch<React.SetStateAction<boolean>>;
}) => {
    return (
        <div className="flex gap-4">
            <Link to="/">
                <Icon type="png" id="Alarm" size={44} />
            </Link>
            <ChatAlarm />
            <div className="relative">
                <button onClick={onClickInfo} className="cursor-pointer">
                    <Icon type="png" id="User" size={44} />
                </button>
                <MyInfoDropDown
                    setActive={setActive}
                    active={infoActive}
                    userInfo={userInfo}
                    addStyle="absolute right-0 translate-y-full -bottom-4"
                    setLogin={setLogin}
                />
            </div>
        </div>
    );
};

const HeaderView = ({
    isLogin,
    setLogin,
    setActive,
    alarmActive,
    infoActive,
    handleSearch,
    searchValue,
    onChangeSearch,
    relatedItem,
    onClickSearch,
    onClickInfo,
    handleLogin,
}: HeaderViewProps) => {
    // 유저 정보 받아와서 렌더링해야함함
    const { userInfo } = useAuth();

    const { setAccessToken } = useTokenStore();
    const loginapi = async (): Promise<void> => {
        try {
            const params = {
                email: 'admin',
                name: 'admin',
            };

            const response = await customFetch('/login/oauth2/yame', {
                method: 'POST',
                params,
            });
            const data = await response.json();
            setAccessToken(data.accessToken);
        } catch (error) {
            console.error('Error fetching area teams:', error);
            throw error;
        }
    };

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
                    <>
                        <LoginedMenu
                            onClickInfo={onClickInfo}
                            userInfo={userInfo}
                            setActive={setActive}
                            setLogin={setLogin}
                            alarmActive={alarmActive}
                            infoActive={infoActive}
                        />
                    </>
                ) : (
                    <>
                        <button
                            onClick={loginapi}
                            className="rounded-lg border border-gray-200 px-2 py-1"
                        >
                            임시 로그인
                        </button>
                        <NoLoginedMenu handleLogin={handleLogin} />
                    </>
                )}
            </div>
        </header>
    );
};

export default HeaderView;
