import { ProfileImageProps } from '@/components/atoms/ProfileImage/ProfileImage';

export interface SimpleTeamResponse {
    teamId: number;
    name: string;
    description: string;
    teamProfileUri: string;
    reviewScore: number;
    tags: string[];
    memberCount: number;
    maxCapacity: number;
    currentCapacity: number;
}
export interface TeamDto {
    teamId: number;
    teamUserId: number;
    profileUri: string;
    name: string;
    description: string;
    recruitStatus: TeamRecruitStatus;
    privateStatus: TeamPrivateStatus;
    area: Area;
    maxCapacity: number;
    currentCapacity: number;
    score: number;
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

export type TeamUserRole = 'LEADER' | 'CO_LEADER' | 'MEMBER';

export interface TeamUserDto {
    teamUserId: number;
    nickname: string;
    role: TeamUserRole;
}

export interface TeamUserResponse {
    size: number;
    hasNext: boolean;
    role: TeamUserRole;
    lastTeamUserId: number;
    users: TeamUserDto[];
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

export interface TeamSimpleScheduleDto {
    teamScheduleId: number;
    date: string;
    title: string;
    price: number;
    profileUris: ProfileImageProps[];
}

export interface TeamSchedulesResponse {
    size: number;
    hasNext: boolean;
    lastTeamScheduleId: number;
    schedules: TeamSimpleScheduleDto[];
}

export interface TeamScheduleSpecificResponse {
    isTeamMember: boolean;
    isTeamScheduleMember: boolean;
    isMyTeamSchedule: boolean;
    teamScheduleId: number;
    status: ScheduleStatus;
    location: string;
    date: Date;
    price: number;
    nameOfLeader: string;
    profileUris: ProfileImageProps[];
    maxParticipants: number;
    currentParticipants: number;
    title: string;
    description: string;
    comments: TeamScheduleCommentDto[];
}

export interface TeamScheduleCommentDto {
    teamScheduleCommentId: number;
    isMyComment: boolean;
    profileUri: string;
    name: string;
    time: string; // LocalDateTime을 string으로 처리
    commentSortId: number; // Long을 number로 처리
    hasParent: boolean;
    content: string;
}

export enum ScheduleStatus {
    AD_HOC = 'AD_HOC', // 번개모임
    REGULAR = 'REGULAR', // 정기모임
    CLOSED = 'CLOSED', // 종료된 모임
}

export const ScheduleStatusName: Record<ScheduleStatus, string> = {
    [ScheduleStatus.AD_HOC]: '번개모임',
    [ScheduleStatus.REGULAR]: '정기모임',
    [ScheduleStatus.CLOSED]: '종료된 모임',
};
