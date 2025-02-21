import { useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';

const GoogleCallback = () => {
    const [searchParams] = useSearchParams();

    useEffect(() => {
        const code = searchParams.get('code');

        if (code) {
            window.opener.postMessage(
                {
                    code: code,
                },
                import.meta.env.VITE_APP_URL,
            );

            window.close();
        }
    }, [searchParams]);

    return (
        <div className="flex h-full w-full items-center justify-center">
            <span className="text-2xl font-semibold">
                토큰을 처리하고 있습니다...
            </span>
        </div>
    );
};

export default GoogleCallback;
