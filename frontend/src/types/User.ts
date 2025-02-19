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
