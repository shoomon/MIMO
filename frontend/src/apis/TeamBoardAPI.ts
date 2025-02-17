import {
    BoardDetailResponse,
    CreateBoardRequest,
    TeamBoardAPIResponse,
    TeamBoardListResponse,
} from '@/types/TeamBoard';
import { customFetch } from './customFetch';

export const getTeamBoardList = async (
    team: string,
): Promise<TeamBoardAPIResponse> => {
    try {
        const response = await customFetch('/team-board', {
            method: 'GET',
            params: { team }, // params는 객체여야 함
        });
        return response.json();
    } catch (error) {
        console.error('Error validTeamName teamName:', error);
        throw error;
    }
};

export const CreateTeamBoard = async (
    teamId: string,
    teamBoardName: string,
): Promise<boolean> => {
    try {
        const body = JSON.stringify({ teamId, teamBoardName });

        const response = await customFetch('/team-board', {
            method: 'POST',
            body, // params는 객체여야 함
        });
        return response.json();
    } catch (error) {
        console.error('Error validTeamName teamName:', error);
        throw error;
    }
};

export const getBoardList = async (
    type: string,
    page?: string,
): Promise<TeamBoardListResponse> => {
    try {
        const params = { type, ...(page ? { page } : {}) };
        const response = await customFetch('/board/list', {
            method: 'GET',
            params,
        });
        return response.json();
    } catch (error) {
        console.error('Error validTeamName teamName:', error);
        throw error;
    }
};

export const createBoard = async (board: CreateBoardRequest): Promise<void> => {
    try {
        const formData = new FormData();
        formData.append('teamBoardId', board.teamBoardId.toString());
        formData.append('teamId', board.teamId.toString());
        formData.append('title', board.title);
        formData.append('description', board.description);

        // 파일이 존재한다면, 배열 내 각 파일을 append 합니다.
        if (board.files && board.files.length > 0) {
            board.files.forEach((file) => {
                // 'files'는 서버에서 기대하는 키 이름에 맞춰 수정하세요.
                formData.append('files', file, file.name);
            });
        }

        await customFetch('/board', {
            method: 'POST',
            body: formData,
        });
    } catch (error) {
        console.error('Error creating board:', error);
        throw error;
    }
};

export const getBoardDetail = async (
    post: string,
): Promise<BoardDetailResponse> => {
    try {
        const response = await customFetch('/board/detail', {
            method: 'GET',
            params: { post }, // params는 객체여야 함
        });
        return response.json();
    } catch (error) {
        console.error('Error validTeamName teamName:', error);
        throw error;
    }
};

export const DeleteBoardComment = async (postId: string): Promise<void> => {
    try {
        const params = {
            id: postId,
        };
        await customFetch('/comment', {
            method: 'DELETE',
            params,
        });
    } catch (error) {
        console.error('Error 댓글 삭제:', error);
        throw error;
    }
};

export const updateBoardComment = async (
    teamId: string,
    commentId: string,
    content: string,
): Promise<void> => {
    try {
        const body = JSON.stringify({
            teamId,
            commentId,
            content,
        });
        await customFetch('/comment', {
            method: 'PUT',
            body,
        });
    } catch (error) {
        console.error('Error updating comment:', error);
        throw error;
    }
};

export const deletePost = async (postId: string): Promise<void> => {
    try {
        const params = {
            post: postId,
        };
        await customFetch('/board', {
            method: 'DELETE',
            params,
        });
    } catch (error) {
        console.error('Error leaving schedule:', error);
        throw error;
    }
};
