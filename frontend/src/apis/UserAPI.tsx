import { findSpecificAlarmResponse, UserAlarmsResponse } from '@/types/User';
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

export const findSpecificAlarm = async (
    alarmId: number,
): Promise<findSpecificAlarmResponse> => {
    try {
        const response = await customFetch(`/alarm/read?alarmId=${alarmId}`, {
            method: 'GET',
        });

        if (!response.ok) {
            throw new Error(
                `Failed to fetch alarm details (Status: ${response.status})`,
            );
        }

        return await response.json();
    } catch (error) {
        console.error('Error fetching alarm specific:', error);
        throw error;
    }
};
