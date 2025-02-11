// hooks/useTeam.ts
import { useParams } from 'react-router-dom';

interface TeamParams {
    teamId: string;
    scheduleId: string;
    [key: string]: string | undefined;
}

export const useTeam = () => {
    // useParams로 URL 파라미터 추출
    const { teamId, scheduleId } = useParams<TeamParams>();

    // 파라미터가 문자열이므로 숫자로 변환 (또는 상황에 맞게 처리)
    const team = {
        id: teamId ? parseInt(teamId, 10) : null,
        scheduleId: scheduleId ? parseInt(scheduleId, 10) : null,
    };

    return { team };
};
