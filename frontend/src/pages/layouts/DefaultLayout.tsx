import { Header, MainNav, NavLevel } from '@/components/molecules';
import { useState } from 'react';
import { Outlet } from 'react-router-dom';

const DefaultLayout = () => {
    const [infoActive, setinfoActive] = useState(false);

    const handleToggleInfo = () => {
        setinfoActive((prev) => !prev);
    };

    return (
        <div className="flex flex-col">
            <Header
                isLogin={true}
                alarmActive={false}
                infoActive={infoActive}
                handleSearch={() => {}}
                searchValue=""
                onChangeSearch={() => {}}
                relatedItem={[]}
                onClickSearch={() => {}}
                onClickAlarm={() => {}}
                onClickInfo={handleToggleInfo}
                handleLogin={() => {}}
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
