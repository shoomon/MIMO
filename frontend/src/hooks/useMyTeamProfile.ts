import { useQuery } from '@tanstack/react-query';
import { getMyTeamProfile } from '@/apis/TeamAPI';

const useMyTeamProfile = (teamId?: string) => {
    return useQuery({
        queryKey: [' MyInfo', teamId],
        queryFn: () => getMyTeamProfile(teamId!),
        enabled: !!teamId, // teamId가 있을 때만 호출
    });
};

export default useMyTeamProfile;
