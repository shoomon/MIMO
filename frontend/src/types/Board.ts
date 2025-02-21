export interface SimpleBoardDTO {
    boardId: number;
    teamBoardId: number;
    boardTitle: string;
    boardCreatedAt: string;
    teamId: number;
    teamName: string;
}

export interface SimpleCommentDTO {
    boardTeamInfo: SimpleBoardDTO;
    commentContent: string;
    createdAt: string;
    imageUri: string;
}
