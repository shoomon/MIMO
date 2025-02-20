import { useQueryClient } from '@tanstack/react-query';
import { useEffect } from 'react';

export const useTeamQRCode = (teamId: string) => {

  const queryClient = useQueryClient();

  const teamData = queryClient.getQueryData(["teamInfo", teamId]);

  useEffect(() => {
    console.log(teamData);
  },[teamData, teamId])
  

  return "";
}