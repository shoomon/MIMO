// src/components/atoms/MyInfoDropDown/MyInfoDropDown.tsx
import React, { useRef, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { ProfileImageProps } from '../ProfileImage/ProfileImage';
import MyInfoDropDownView from './MyInfoDropDown.view';
import { useTokenStore } from '@/stores/tokenStore';
import { useQueryClient } from '@tanstack/react-query';
import { logoutapi } from '@/apis/AuthAPI';

interface MyInfoDropDownProps {
    userInfo?: ProfileImageProps;
    active: boolean;
    addStyle: string;
    setActive: React.Dispatch<React.SetStateAction<boolean>>;
    setLogin: React.Dispatch<React.SetStateAction<boolean>>;
}

const MyInfoDropDown = ({
    userInfo,
    active,
    addStyle,
    setActive,
    setLogin,
}: MyInfoDropDownProps) => {
    const { logout } = useTokenStore();
    const queryClient = useQueryClient();
    const navigate = useNavigate();
    const location = useLocation();

    // 드롭다운 영역을 감싸는 ref 생성
    const dropdownRef = useRef<HTMLDivElement>(null);

    const handleLogout = async () => {
        logout();
        await logoutapi();
        queryClient.removeQueries({ queryKey: ['userInfo'] });
        setActive(false);
        setLogin(false);
        navigate('/');
    };

    // 외부 클릭 시 드롭다운 닫기
    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (
                dropdownRef.current &&
                !dropdownRef.current.contains(event.target as Node)
            ) {
                setActive(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () =>
            document.removeEventListener('mousedown', handleClickOutside);
    }, [setActive]);

    // 페이지 이동 시 드롭다운 닫기
    useEffect(() => {
        setActive(false);
    }, [location, setActive]);

    // 모든 Hook 호출 후, userInfo가 없으면 null 반환
    if (!userInfo) {
        return null;
    }

    return (
        <div ref={dropdownRef}>
            <MyInfoDropDownView
                userInfo={userInfo}
                active={active}
                addStyle={addStyle}
                onClick={handleLogout}
            />
        </div>
    );
};

export default MyInfoDropDown;
