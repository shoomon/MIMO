import { Header, MainNav, NavLevel } from '@/components/molecules';
import { useAuth, useOauth } from '@/hooks/useAuth';
import { useState } from 'react';
import { Outlet } from 'react-router-dom';

const DefaultLayout = () => {
    const [infoActive, setinfoActive] = useState<boolean>(false);

    const handleToggleInfo = () => {
        setinfoActive((prev) => !prev);
    };

    const { handleLogin } = useOauth();
    const { isLogin } = useAuth();

    return (
        <>
            <Header
                isLogin={isLogin}
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

            <div className="flex flex-1 flex-col gap-3">
                <MainNav />
                <NavLevel />
                <Outlet />
            </div>
        </>
    );
};

export default DefaultLayout;
