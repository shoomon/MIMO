import { customFetch } from './customFetch';
import { TeamInfosResponse } from '@/types/Team';

const TeamApi = {
    getTeamInfosByCategory: async (
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
                credentials: 'include', // HttpOnly 쿠키를 위한 설정
            });

            if (!response.ok) {
                throw new Error('Failed to fetch category teams');
            }

            return response.json();
        } catch (error) {
            console.error('Error fetching category teams:', error);
            throw error;
        }
    },

    getTeamInfosByArea: async (
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

            if (!response.ok) {
                throw new Error('Failed to fetch area teams');
            }

            return response.json();
        } catch (error) {
            console.error('Error fetching area teams:', error);
            throw error;
        }
    },
};

export default TeamApi;
