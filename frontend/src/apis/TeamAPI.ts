import { customFetch } from './customFetch';
import {
    Area,
    TeamInfosResponse,
    TeamScheduleSpecificResponse,
    TeamSchedulesResponse,
} from '@/types/Team';

export const getTeamInfosByCategory = async (
    category: string,
    teamId?: number,
): Promise<TeamInfosResponse> => {
    try {
        const params = {
            category,
            ...(teamId && { teamId: teamId.toString() }),
        };

        const response = await customFetch('/team/category', {
            method: 'GET',
            params,
        });

        return response.json();
    } catch (error) {
        console.error('Error fetching category teams:', error);
        throw error;
    }
};

export const getTeamInfosByArea = async (
    area: Area,
    teamId?: number,
): Promise<TeamInfosResponse> => {
    try {
        const params = {
            area,
            ...(teamId && { teamId: teamId.toString() }),
        };

        const response = await customFetch('/team/area', {
            method: 'GET',
            params,
        });

        return response.json();
    } catch (error) {
        console.error('Error fetching area teams:', error);
        throw error;
    }
};

export const getAdhocSchedules = async (
    teamId: number,
    lastTeamScheduleId?: number,
): Promise<TeamSchedulesResponse> => {
    try {
        const params = {
            teamId: teamId.toString(),
            ...(lastTeamScheduleId && {
                lastTeamScheduleId: lastTeamScheduleId.toString(),
            }),
        };

        const response = await customFetch('/team-schedule/ad-hoc', {
            method: 'GET',
            params,
        });

        return response.json();
    } catch (error) {
        console.error('Error fetching adhoc schedules:', error);
        throw error;
    }
};

export const getRegularSchedules = async (
    teamId: number,
    lastTeamScheduleId?: number,
): Promise<TeamSchedulesResponse> => {
    try {
        const params = {
            teamId: teamId.toString(),
            ...(lastTeamScheduleId && {
                lastTeamScheduleId: lastTeamScheduleId.toString(),
            }),
        };

        const response = await customFetch('/team-schedule/regular', {
            method: 'GET',
            params,
        });

        return response.json();
    } catch (error) {
        console.error('Error fetching regular schedules:', error);
        throw error;
    }
};

export const getClosedSchedules = async (
    teamId: number,
    lastTeamScheduleId?: number,
): Promise<TeamSchedulesResponse> => {
    try {
        const params = {
            teamId: teamId.toString(),
            ...(lastTeamScheduleId && {
                lastTeamScheduleId: lastTeamScheduleId.toString(),
            }),
        };

        const response = await customFetch('/team-schedule/closed', {
            method: 'GET',
            params,
        });

        return response.json();
    } catch (error) {
        console.error('Error fetching closed schedules:', error);
        throw error;
    }
};

export const getSpecificSchedule = async (
    teamId: number,
    teamScheduleId: number,
): Promise<TeamScheduleSpecificResponse> => {
    try {
        const params = {
            teamId: teamId.toString(),
            teamScheduleId: teamScheduleId.toString(),
        };

        const response = await customFetch('/team-schedule', {
            method: 'GET',
            params,
        });

        return response.json();
    } catch (error) {
        console.error('Error fetching specific schedule:', error);
        throw error;
    }
};

export const joinSchedule = async (teamScheduleId: number): Promise<void> => {
    try {
        const body = JSON.stringify({ teamScheduleId });
        await customFetch('/schedule-participants', {
            method: 'POST',
            body,
        });
    } catch (error) {
        console.error('Error joining schedule:', error);
        throw error;
    }
};

/**
 * 일정에서 탈퇴하는 API
 */

export const leaveSchedule = async (
    teamScheduleId: number,
    userId: number,
): Promise<void> => {
    try {
        const body = JSON.stringify({ teamScheduleId, userId });
        await customFetch('/schedule-participants', {
            method: 'DELETE',
            body,
        });
    } catch (error) {
        console.error('Error leaving schedule:', error);
        throw error;
    }
};
