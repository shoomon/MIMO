export interface SimpleTeamResponse {
    teamId: number;
    name: string;
    description: string;
    teamProfileUri: string;
    reviewScore: number;
    tags: string[];
}

export interface TeamResponse {
    teamId: number;
    profileUri: string;
    name: string;
    description: string;
    recruitStatus: TeamRecruitStatus;
    privateStatus: TeamPrivateStatus;
    area: Area;
    maxCapacity: number;
    currentCapacity: number;
}

export interface TeamInfosResponse {
    size: number;
    hasNext: boolean;
    lastTeamId: number;
    teams: SimpleTeamResponse[];
}

export enum TeamRecruitStatus {
    RECRUITING = 'RECRUITING',
    CLOSED = 'CLOSED',
}

export enum TeamPrivateStatus {
    PUBLIC = 'PUBLIC',
    PRIVATE = 'PRIVATE',
}

export enum Area {
    GYEONGGI = 'GYEONGGI',
    GANGWON = 'GANGWON',
    CHUNGCHEONG_NORTH = 'CHUNGCHEONG_NORTH',
    CHUNGCHEONG_SOUTH = 'CHUNGCHEONG_SOUTH',
    JEOLLA_NORTH = 'JEOLLA_NORTH',
    JEOLLA_SOUTH = 'JEOLLA_SOUTH',
    GYEONGSANG_NORTH = 'GYEONGSANG_NORTH',
    GYEONGSANG_SOUTH = 'GYEONGSANG_SOUTH',
    JEJU = 'JEJU',
    SEJONG = 'SEJONG',
}

export const AreaName: Record<Area, string> = {
    [Area.GYEONGGI]: '서울, 경기도',
    [Area.GANGWON]: '강원도',
    [Area.CHUNGCHEONG_NORTH]: '충청북도',
    [Area.CHUNGCHEONG_SOUTH]: '충청남도',
    [Area.JEOLLA_NORTH]: '전라북도',
    [Area.JEOLLA_SOUTH]: '전라남도',
    [Area.GYEONGSANG_NORTH]: '경상북도',
    [Area.GYEONGSANG_SOUTH]: '경상남도',
    [Area.JEJU]: '제주특별자치도',
    [Area.SEJONG]: '세종특별자치시',
};
