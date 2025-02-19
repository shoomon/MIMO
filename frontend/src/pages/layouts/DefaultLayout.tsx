import { Header, MainNav, NavLevel } from '@/components/molecules';
import { useAuth, useOauth } from '@/hooks/useAuth';
import { useState } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';

const DefaultLayout = () => {
    const navigate = useNavigate();

    const { handleLogin } = useOauth();
    const { isLogin, setLogin } = useAuth();

    const [searchValue, setSearchValue] = useState<string>('');

    // 검색어 입력 핸들러
    const onChangeSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchValue(e.target.value);
    };

    // 검색 실행 핸들러
    const handleSearch = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (searchValue.trim()) {
            navigate(
                `/search/title?searchKeyword=${encodeURIComponent(searchValue)}&pageNumber=1`,
            );
        }
    };

    return (
        <>
            <Header
                isLogin={isLogin}
                setLogin={setLogin}
                handleSearch={handleSearch}
                searchValue={searchValue}
                onChangeSearch={onChangeSearch}
                relatedItem={[]} // 검색어 자동완성 기능 추가 가능
                onClickSearch={() => {}}
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
