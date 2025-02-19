// src/components/molecules/Header/HeaderView.tsx
import React, { useState } from 'react';
import { ChatAlarm, Icon, Logo } from '@/components/atoms';
import { Link } from 'react-router-dom';
import SearchBar from '../SearchBar/SearchBar';
import { ProfileImageProps } from '@/components/atoms/ProfileImage/ProfileImage';
import MyInfoDropDown from '@/components/atoms/MyInfoDropDown/MyInfoDropDown';
import { customFetch } from '@/apis/customFetch';
import { useTokenStore } from '@/stores/tokenStore';
import { useAuth } from '@/hooks/useAuth';
import AlarmView from './../Alarm/Alarm.view';

export interface HeaderViewProps {
    isLogin: boolean;
    setLogin: React.Dispatch<React.SetStateAction<boolean>>;
    handleSearch: (e: React.FormEvent<HTMLFormElement>) => void;
    searchValue: string;
    onChangeSearch: (e: React.ChangeEvent<HTMLInputElement>) => void;
    relatedItem: string[];
    onClickSearch: (e: React.MouseEvent<HTMLButtonElement>) => void;
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

interface LoginedMenuProps {
    userInfo?: ProfileImageProps;
    alarmActive: boolean;
    userInfoActive: boolean;
    handleAlarmClick: () => void;
    handleUserClick: () => void;
    setLogin: React.Dispatch<React.SetStateAction<boolean>>;
    setAlarmActive: React.Dispatch<React.SetStateAction<boolean>>;
    setUserInfoActive: React.Dispatch<React.SetStateAction<boolean>>;
}

const LoginedMenu = ({
    userInfo,
    userInfoActive,
    handleUserClick,
    setLogin,
    setUserInfoActive,
}: LoginedMenuProps) => {
    return (
        <div className="flex gap-4">
            {/* 알람 드롭다운 영역 */}
            <div className="relative">
                <AlarmView />
            </div>
            <ChatAlarm />
            {/* 내 정보 드롭다운 영역 */}
            <div className="relative">
                <button onClick={handleUserClick} className="cursor-pointer">
                    <Icon type="png" id="User" size={44} />
                </button>
                <MyInfoDropDown
                    setActive={setUserInfoActive}
                    active={userInfoActive}
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
    handleSearch,
    searchValue,
    onChangeSearch,
    relatedItem,
    onClickSearch,
    handleLogin,
}: HeaderViewProps) => {
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

    // 알람 드롭다운과 내 정보 드롭다운의 상태를 별도로 관리
    const [alarmActive, setAlarmActive] = useState(false);
    const [userInfoActive, setUserInfoActive] = useState(false);

    const handleAlarmClick = () => {
        setAlarmActive((prev) => !prev);
        // 내 정보 드롭다운은 닫기
        setUserInfoActive(false);
    };

    const handleUserClick = () => {
        setUserInfoActive((prev) => !prev);
        // 알람 드롭다운은 닫기
        setAlarmActive(false);
    };

    return (
        <header className="w-full py-5">
            <div className="flex flex-wrap items-center justify-between gap-4">
                <h1>
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

                {isLogin ? (
                    <LoginedMenu
                        handleAlarmClick={handleAlarmClick}
                        handleUserClick={handleUserClick}
                        userInfo={userInfo}
                        alarmActive={alarmActive}
                        userInfoActive={userInfoActive}
                        setLogin={setLogin}
                        setAlarmActive={setAlarmActive}
                        setUserInfoActive={setUserInfoActive}
                    />
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
