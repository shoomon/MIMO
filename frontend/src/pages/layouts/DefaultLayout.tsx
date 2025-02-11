import { getToken, oauthAPI } from '@/apis/authAPI';
import { Header, MainNav, NavLevel } from '@/components/molecules';
import { useEffect, useState } from 'react';
import { Outlet } from 'react-router-dom';

const DefaultLayout = () => {
    const [infoActive, setinfoActive] = useState<boolean>(false);
    const [oauthToken, setOauthToken] = useState<string>('');

    const handleToggleInfo = () => {
        setinfoActive((prev) => !prev);
    };

    const handleLogin = () => {
        oauthAPI();
    };

    // useEffect(() => {
    //     getToken(oauthToken);
    // }, [oauthToken]);

    useEffect(() => {
        const messageHandler = (e: MessageEvent) => {
            const data = e.data;
            setOauthToken(data.code);
            console.log('메시지', data.code);
        };

        window.addEventListener('message', messageHandler);

        return () => {
            window.removeEventListener('message', messageHandler);
        };
    }, []);

    return (
        <div className="flex flex-col px-4">
            <Header
                isLogin={false}
                alarmActive={false}
                infoActive={infoActive}
                handleSearch={() => {}}
                searchValue=""
                onChangeSearch={() => {}}
                relatedItem={[]}
                onClickSearch={() => {}}
                onClickAlarm={() => {}}
                onClickInfo={handleToggleInfo}
                handleLogin={handleLogin}
            />
            <div className="flex flex-col gap-3">
                <MainNav />
                <NavLevel />
                <Outlet />
            </div>
        </div>
    );
};

export default DefaultLayout;
