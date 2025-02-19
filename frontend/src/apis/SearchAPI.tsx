import { TeamInfosResponse } from '@/types/Team';
import { customFetch } from './customFetch';

export const TeamTitleSearch = async (
    searchKeyword: string,
    pageNumber?: string,
): Promise<TeamInfosResponse> => {
    try {
        const params: Record<string, string> = { searchKeyword };
        if (pageNumber) params.pageNumber = pageNumber;

        // API 호출
        const response = await customFetch('/search-team/title-description', {
            method: 'GET',
            params,
        });

        return await response.json();
    } catch (error) {
        console.error('Error fetching search:', error);
        throw error;
    }
};
