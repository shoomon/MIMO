import {
    BoardDetailResponse,
    CreateBoardRequest,
    TeamBoardAPIResponse,
    TeamBoardListResponse,
} from '@/types/TeamBoard';
import { customFetch } from './customFetch';
import { AlbumItemProps } from '@/components/molecules/Album/Album.view';

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
        return response.status == 200;
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

export const likeBoard = async (
    teamUserId: string,
    boardId: string,
): Promise<void> => {
    try {
        const body = JSON.stringify({
            teamUserId,
            boardId,
        });
        console.log(body);

        await customFetch('/board/like', {
            method: 'POST',
            body,
        });
    } catch (error) {
        console.error('Error updating comment:', error);
        throw error;
    }
};

export const deleteTeamBoard = async (
    teamBoardId: string,
): Promise<boolean> => {
    try {
        const params = {
            board: teamBoardId,
        };
        const response = await customFetch('/team-board', {
            method: 'DELETE',
            params,
        });
        return response.status == 200;
    } catch (error) {
        console.error('Error 보드삭제:', error);
        throw error;
    }
};

export const updateBoard = async (
    postId: string,
    title: string,
    description: string,
    filesToDelete: string,
    newFiles: File[],
): Promise<void> => {
    try {
        // 1) FormData 생성
        const formData = new FormData();

        // 2) 텍스트 필드
        formData.append('post', postId);
        formData.append('title', title);
        formData.append('description', description);
        formData.append('filesToDelete', filesToDelete);
        // filesToDelete는 예: '[{"fileId":3,"fileExtension":"jpg","fileUri":"..."}]'

        // 3) 새 파일들 (binary)
        newFiles.forEach((file) => {
            // 서버가 'filesToAdd'라는 키로 여러 파일을 받도록 설정
            formData.append('filesToAdd', file);
        });

        // 4) PUT 요청 (multipart/form-data)
        await customFetch('/board', {
            method: 'PUT',
            // 필요하다면 params: { post: postId } 등 붙이세요
            body: formData,
            // Content-Type은 직접 설정하지 말고 FormData로 넘기면 자동 설정됩니다.
        });
    } catch (error) {
        console.error('Error updating board:', error);
        throw error;
    }
};

export const getAlbumImageList = async (
    teamId: string,
): Promise<AlbumItemProps[]> => {
    try {
        const params = { team: teamId };
        const response = await customFetch('/team-image', {
            method: 'GET',
            params,
        });
        return await response.json(); //
    } catch (error) {
        console.error('Error 보드삭제:', error);
        throw error;
    }
};
