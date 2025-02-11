import { customFetch } from './customFetch';
import {
    Area,
    TeamDto,
    TeamInfosResponse,
    TeamScheduleSpecificResponse,
    TeamSchedulesResponse,
} from '@/types/Team';

export const getTeamInfo = async (teamId: string): Promise<TeamDto> => {
    if (!teamId) {
        throw new Error('팀 아이디가 없습니다.');
    }

    try {
        const response = await customFetch('/team', {
            method: 'GET',
            params: { teamId }, // params는 객체여야 함
        });

        return response.json();
    } catch (error) {
        console.error('Error fetching category teams:', error);
        throw error;
    }
};

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
            credentials: 'include',
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

        const response = await customFetch('/schedule/ad-hoc', {
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

        const response = await customFetch('/schedule/regular', {
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

        const response = await customFetch('/schedule/closed', {
            method: 'GET',
            params,
        });

        return response.json();
    } catch (error) {
        console.error('Error fetching closed schedules:', error);
        throw error;
    }
};

export const createSchedule = async (
    teamId: number,
    userId: number,
    title: string,
    description: string,
    location: string,
    date: string,
    maxParticipants: number,
    price: number,
    status: 'REGULAR' | 'AD_HOC',
): Promise<void> => {
    try {
        const body = JSON.stringify({
            teamId,
            userId,
            title,
            description,
            location,
            date,
            maxParticipants,
            price,
            status,
        });

        await customFetch('/schedule', {
            method: 'POST',
            body,
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        alert('일정이 성공적으로 등록되었습니다!');
    } catch (error) {
        console.error('Error creating schedule:', error);
        alert('일정 등록 중 오류가 발생했습니다.');
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

        const response = await customFetch('/schedule', {
            method: 'GET',
            params,
            credentials: 'include',
        });

        return response.json();
    } catch (error) {
        console.error('Error fetching specific schedule:', error);
        throw error;
    }
};

export const joinSchedule = async (teamScheduleId: string): Promise<void> => {
    try {
        const params = { teamScheduleId: teamScheduleId };
        await customFetch('/schedule-participants', {
            method: 'POST',
            params,
            credentials: 'include',
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

export const deleteComment = async (commentId: number): Promise<void> => {
    try {
        const body = JSON.stringify({ commentId });
        await customFetch('/schedule-comment', {
            method: 'DELETE',
            body,
        });
    } catch (error) {
        console.error('Error deleting comment:', error);
        throw error;
    }
};

export const updateComment = async (
    userId: number,
    teamScheduleId: number,
    commentId: number,
    content: string,
): Promise<void> => {
    try {
        const body = JSON.stringify({
            userId,
            teamScheduleId,
            commentId,
            content,
        });
        await customFetch('/schedule-comment', {
            method: 'PUT',
            body,
        });
    } catch (error) {
        console.error('Error updating comment:', error);
        throw error;
    }
};

export const createComment = async (
    userId: number,
    teamId: number,
    teamScheduleId: number,
    commentId: number,
    teamUserId: number,
    parentCommentId: number | null,
    content: string,
): Promise<void> => {
    try {
        const body = JSON.stringify({
            userId,
            teamId,
            teamScheduleId,
            commentId,
            teamUserId,
            parentCommentId,
            content,
        });
        await customFetch('/schedule-comment', {
            method: 'POST',
            body,
            credentials: 'include',
        });
    } catch (error) {
        console.error('Error creating comment:', error);
        throw error;
    }
};
