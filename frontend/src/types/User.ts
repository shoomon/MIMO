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
