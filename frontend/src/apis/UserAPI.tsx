import {
    findSpecificAlarmResponse,
    UserAlarmsResponse,
    getTeamReviewResponse,
} from '@/types/User';
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

export const remainTeamReview = async (
    teamId: string,
    memo: string,
    score: string,
): Promise<void> => {
    try {
        const param = { teamId, memo, score };

        const response = await customFetch('/team-review', {
            method: 'POST',
            params: param,
        });
        return response.json();
    } catch (error) {
        console.error('Error in remainTeamReview:', error);
        throw error;
    }
};

export const getTeamReview = async (
    teamId: string,
): Promise<getTeamReviewResponse> => {
    try {
        const param = { teamId };

        const response = await customFetch('/team-review', {
            method: 'GET',
            params: param,
        });
        return response.json();
    } catch (error) {
        console.error('Error in remainTeamReview:', error);
        throw error;
    }
};

export const deleteTeamReview = async (teamId: string): Promise<void> => {
    try {
        const param = { teamId };

        const response = await customFetch('/team-review', {
            method: 'DELETE',
            params: param,
        });
        return response.json();
    } catch (error) {
        console.error('Error in remainTeamReview:', error);
        throw error;
    }
};
