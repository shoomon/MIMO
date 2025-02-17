// 전체 API 응답에 대한 타입
export interface TeamBoardAPIResponse {
    teamBoardList: TeamBoardListResponse[];
}

export interface TeamBoardListResponse {
    teamBoardId?: number;
    teamBoardName: string;
    boardList: Post[];
}

export interface Post {
    boardId: number;
    userProfileUri: string;
    userNickname: string;
    postTitle: string;
    imageUri: string;
    likeCount: number;
    viewCount: number;
    createdAt: string;
    updatedAt: string;
    commentCount: number;
}

export interface CreateBoardRequest {
    teamBoardId: number;
    teamId: number;
    title: string;
    description: string;
    files?: File[];
}
export type layoutType = 'List' | 'Card';

export interface PostDetail {
    postId: number;
    userId: number;
    teamUserId: number;
    userProfileUri: string;
    userNickname: string | null;
    boardName: string;
    postTitle: string;
    description: string;
    likeCount: number;
    viewCount: number;
    createdAt: string;
    updatedAt: string;
}

export interface BoardFile {
    fileId: number;
    fileExtension: string;
    fileUri: string;
}

// 댓글 기본 정보 (루트 댓글과 자식 댓글 모두 동일한 구조)
export interface Comment {
    commentId: number;
    parentId: number | null;
    userId: number;
    userNickname: string | null;
    userProfileImage: string;
    content: string;
    createdAt: string;
    updatedAt: string;
}

// 하나의 댓글 스레드 (루트 댓글과 그에 대한 자식 댓글들)
export interface CommentThread {
    rootComment: Comment;
    comments: Comment[];
}

// 전체 응답 구조
export interface BoardDetailResponse {
    board: PostDetail;
    files: BoardFile[];
    userLiked: boolean;
    comments: CommentThread[];
}
