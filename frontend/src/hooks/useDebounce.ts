import { useState, useEffect } from 'react';

/**
 * useDebounce
 * @param value - 입력값
 * @param delay - 디바운스 지연 시간 (ms)
 * @returns 디바운스 처리된 값
 */
export function useDebounce<T>(value: T, delay: number): T {
    const [debouncedValue, setDebouncedValue] = useState<T>(value);

    useEffect(() => {
        const handler = setTimeout(() => {
            setDebouncedValue(value);
        }, delay);

        return () => {
            clearTimeout(handler);
        };
    }, [value, delay]);

    return debouncedValue;
}
