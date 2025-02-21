import { useState, useEffect } from 'react';

interface LocationData {
    region: string;
    address: string;
}

const useLocation = () => {
    const [locationData, setLocationData] = useState<LocationData | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!navigator.geolocation) {
            setError('Geolocation API를 지원하지 않습니다.');
            setLoading(false);
            return;
        }

        navigator.geolocation.getCurrentPosition(
            async (position) => {
                const { latitude, longitude } = position.coords;
                try {
                    const response = await fetch(
                        `https://nominatim.openstreetmap.org/reverse?format=json&lat=${latitude}&lon=${longitude}&accept-language=ko`,
                    );
                    const data = await response.json();

                    // data.address 예시:
                    // { road: 'OO로', suburb: 'OO동', city: '서울특별시', state: '서울특별시', country: '대한민국', ... }
                    let region = '';
                    if (data.address) {
                        if (data.address.state) {
                            // "서울특별시"인 경우 "서울"로 변환, 나머지는 그대로 사용(보통 "경기도", "충청남도" 등)
                            if (data.address.state.includes('서울')) {
                                region = '서울';
                            } else {
                                region = data.address.state;
                            }
                        } else if (data.address.city) {
                            // state가 없으면 city로 대체
                            if (data.address.city.includes('서울')) {
                                region = '서울';
                            } else {
                                region = data.address.city;
                            }
                        }
                    }

                    setLocationData({
                        region,
                        address: data.display_name,
                    });
                    setLoading(false);
                } catch (err: unknown) {
                    console.error('Reverse Geocoding 오류:', err);
                    setError('주소 정보를 가져오지 못했습니다.');
                    setLoading(false);
                }
            },
            (error) => {
                console.error('위치 정보를 가져오는 중 오류 발생:', error);
                setError(error.message);
                setLoading(false);
            },
        );
    }, []);

    return { locationData, loading, error };
};

export default useLocation;
