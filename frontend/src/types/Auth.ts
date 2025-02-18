import { SimpleBoardDTO, SimpleCommentDTO } from './Board';

export interface OAuthResponse {
    accessToken: string;
}

export interface UserResponse {
    nickname: string;
    profileUri: string;
}

export interface boardTeamInfo {
    boardTeamInfo: SimpleBoardDTO;
    imageUri: string;
}

export interface UserMyResponse {
    name: string;
    nickname: string;
    email: string;
    profileUri: string;
    reviewScore: number;
    mileage: number;
    mileageIncome: number;
    mileageOutcome: number;
    userBoard: boardTeamInfo[];
    userComment: SimpleCommentDTO[];
}
