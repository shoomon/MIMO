import { UserAlarmsResponse } from '@/types/User';
import { customFetch } from './customFetch';

export const getMyAlarm = async (): Promise<UserAlarmsResponse> => {
    try {
        const response = await customFetch('/alarm', {
            method: 'GET',
        });

        return response.json();
    } catch (error) {
        console.error('Error fetching alarm:', error);
        throw error;
    }
};
