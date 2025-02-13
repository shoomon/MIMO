import { TeamData } from '@/pages/Team/TeamCreate';
import { customFetch } from './customFetch';
import {
    Area,
    TeamDto,
    TeamInfosResponse,
    TeamScheduleSpecificResponse,
    TeamSchedulesResponse,
    TeamUserResponse,
} from '@/types/Team';

export const createTeam = async (team: TeamData): Promise<void> => {
    try {
        // plain object를 FormData로 변환 (파일 유무에 상관없이)
        const formData = new FormData();
        formData.append('name', team.name);
        formData.append('description', team.description);
        formData.append('area', team.area);
        formData.append('category', team.category);
        formData.append('nickname', team.nickname);
        formData.append('notificationStatus', team.notificationStatus);
        formData.append('teamRecruitStatus', team.teamRecruitStatus);
        formData.append('teamPrivateStatus', team.teamPrivateStatus);
        formData.append('maxCapacity', team.maxCapacity);
        if (team.teamProfile) {
            formData.append(
                'teamProfile',
                team.teamProfile,
                team.teamProfile.name,
            );
        }

        await customFetch('/team', {
            method: 'POST',
            body: formData,
        });
    } catch (error) {
        console.error('Error creating team:', error);
        throw error;
    }
};

export const validTeamName = async (name: string): Promise<boolean> => {
    if (!name) {
        throw new Error('팀 아이디가 없습니다.');
    }

    try {
        const response = await customFetch('/team/exist-name', {
            method: 'GET',
            params: { name }, // params는 객체여야 함
        });
        return response.json();
    } catch (error) {
        console.error('Error validTeamName teamName:', error);
        throw error;
    }
};

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

export const getTeamUsers = async (
    teamId: string,
): Promise<TeamUserResponse> => {
    if (!teamId) {
        throw new Error('팀 아이디가 없습니다.');
    }

    try {
        const response = await customFetch('/team-user/users', {
            method: 'GET',
            params: { teamId }, // params는 객체여야 함
        });

        return response.json();
    } catch (error) {
        console.error('Error fetching category teams:', error);
        throw error;
    }
};

export const deleteUsers = async (
    teamId: string,
    teamUserId: string,
): Promise<void> => {
    try {
        const body = JSON.stringify({
            teamId,
            teamUserId,
        });

        await customFetch('/team-leader', {
            method: 'DELETE',
            body,
        });

        //  if(response.status==200){

        //  }
    } catch (error) {
        console.error('Error fetching delete team user:', error);
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
        });

        alert('일정이 성공적으로 등록되었습니다!');
    } catch (error) {
        console.error('Error creating schedule:', error);
        alert('일정 등록 중 오류가 발생했습니다.');
    }
};
export const updateSchedule = async (
    teamId: number,
    teamScheduleId: number,
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
            teamScheduleId,
            title,
            description,
            location,
            date,
            maxParticipants,
            price,
            status,
        });

        await customFetch(`/schedule`, {
            method: 'PUT',
            body,
            credentials: 'include',
        });
    } catch (error) {
        console.error('Error updating schedule:', error);
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
        });
    } catch (error) {
        console.error('Error joining schedule:', error);
        throw error;
    }
};

/**
 * 일정에서 탈퇴하는 API
 */

export const leaveSchedule = async (teamScheduleId: string): Promise<void> => {
    try {
        const params = {
            teamScheduleId: teamScheduleId,
        };
        await customFetch('/schedule-participants', {
            method: 'DELETE',
            params,
        });
    } catch (error) {
        console.error('Error leaving schedule:', error);
        throw error;
    }
};

export const deleteSchedule = async (
    teamId: string,
    teamScheduleId: string,
): Promise<void> => {
    try {
        const params = {
            teamId: teamId,
            teamScheduleId: teamScheduleId,
        };
        await customFetch('/schedule', {
            method: 'DELETE',
            params,
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
    teamId: string,
    teamScheduleCommentId: number,
    content: string,
): Promise<void> => {
    try {
        const body = JSON.stringify({
            teamId,
            teamScheduleCommentId,
            content,
        });
        await customFetch('/schedule-comment', {
            method: 'PATCH',
            body,
        });
    } catch (error) {
        console.error('Error updating comment:', error);
        throw error;
    }
};

export const createComment = async (
    teamId: string,
    teamScheduleId: string,
    teamUserId: number,
    content: string,
): Promise<void> => {
    try {
        const body = JSON.stringify({
            teamId,
            teamScheduleId,
            teamUserId,
            content,
        });
        await customFetch('/schedule-comment', {
            method: 'POST',
            body,
        });
    } catch (error) {
        console.error('Error creating comment:', error);
        throw error;
    }
};
