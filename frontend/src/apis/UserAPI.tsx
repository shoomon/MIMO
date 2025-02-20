import {
    findSpecificAlarmResponse,
    UserAlarmsResponse,
    getTeamReviewResponse,
} from '@/types/User';
import { customFetch } from './customFetch';
import { TagsResponse } from '@/types/Team';

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

        await customFetch('/team-review', {
            method: 'POST',
            params: param,
        });
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

export const updateNickname = async (
    teamId: string,
    nickname: string,
): Promise<void> => {
    try {
        const body = JSON.stringify({ teamId, nickname });
        const response = await customFetch('/team-user', {
            method: 'PATCH',
            body,
        });

        if (!response.ok) {
            const errorData = await response.json();
            if (response.status == 500) {
                throw new Error('중복된 닉네임입니다.');
            } else {
                throw new Error(errorData.message);
            }
        }
    } catch (error) {
        console.error('Error in updateNickname:', error);
        throw error;
    }
};

export const addTag = async (teamId: string, tags: string[]): Promise<void> => {
    try {
        const params = { teamId };
        const body = JSON.stringify({ tags });
        await customFetch('/team-leader/tag', {
            method: 'POST',
            params,
            body,
        });
    } catch (error) {
        console.error('Error in addTag:', error);
        throw error;
    }
};
