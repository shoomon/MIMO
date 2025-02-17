import { getMyAllInfoAPI } from '@/apis/AuthAPI';
import { useQuery } from '@tanstack/react-query';
import { useEffect } from 'react';

const MyPage = () => {
    const { data, isSuccess } = useQuery({
        queryKey: ['myAllData'],
        queryFn: getMyAllInfoAPI,
        staleTime: 1000 * 30,
        gcTime: 1000 * 60,
    });

    useEffect(() => {
        console.log(data);
    }, [data, isSuccess]);

    return <div>내정보</div>;
};

export default MyPage;
