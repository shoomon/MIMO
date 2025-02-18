import { getTeamBalanceAPI, getTeamPayDetailAPI } from "@/apis/AccountAPI";
import { MileageStatusProps } from "@/components/atoms/MileageStatus/MileageStatus";
import { useQuery } from "@tanstack/react-query";
import { useMemo } from "react";
import { useParams } from "react-router-dom";

const useTeamMileage = () => {

  const { teamId } = useParams();

  const { data: teamBalance } = useQuery({
    queryKey: ["teamBalance", teamId],
    queryFn: async () => {
      if (!teamId) throw new Error("Team ID is required");
      return getTeamBalanceAPI(teamId);
    },
    staleTime: 1000 * 20,
  })

  const { data: teamPayDetail } = useQuery({
    queryKey: ["teamPayDetail", teamId],
    queryFn: async () => {
      if (!teamId) throw new Error("Team ID is required");
      return getTeamPayDetailAPI(teamId);
    },
    staleTime: 1000 * 20,
  })

  const teamMileageData:MileageStatusProps[] = useMemo(() => {
  
      if(!teamBalance || !teamPayDetail){

        console.log("마일리지 데이터가 없습니다..");
        return [];
      }
  
      const balanceData = {type:"balance" as const, amount:teamBalance};
      const expenseData = {type:"expense" as const, amount: teamPayDetail.reduce((acc, cur) => acc + cur.amount, 0)}
     
  
      return [balanceData, expenseData]
  
    },[teamBalance, teamPayDetail])

  
  return {teamMileageData}
}

export default useTeamMileage;