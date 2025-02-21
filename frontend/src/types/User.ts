export interface UserAlarm {
    alarmId: number;
    userId: number;
    scheduleId: number;
    title: string;
    description: string;
}

export interface UserAlarmsResponse {
    userAlarms: UserAlarm[];
}

export interface findSpecificAlarmResponse {
    alarm: alarm;
}
export interface alarm {
    alarmId: number;
    userId: number;
    teamId: string;
    scheduleId: number;
    title: string;
    description: string;
}

export interface getTeamReviewResponse {
    size: number;
    hasNext: boolean;
    lastReviewId: number;
    reviews: review[];
}

export interface review {
    teamReviewId: number;
    memo: string;
    score: number;
    createAt: string;
}

export interface UserProfileRequest {
    nickname: string;
    name: string;
    profile: File;
}
