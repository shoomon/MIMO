import { getTeamBalanceAPI, getTeamPayDetailAPI } from "@/apis/AccountAPI";
import { getMyPayerCheckAPI, getTeamNonPayerDetailsAPI, getTeamPayerDetailsAPI } from "@/apis/IntsallmentAPI";
import { MileageStatusProps } from "@/components/atoms/MileageStatus/MileageStatus";
import { RawDataRow } from "@/utils/transformTableData";
import { useQuery } from "@tanstack/react-query";
import { useMemo } from "react";

const useTeamMileage = (teamId:string, round:string) => {

  const { data: teamBalance, isSuccess:balanceSuccess } = useQuery({
    queryKey: ["teamBalance", teamId],
    queryFn: async () => {
      if (!teamId) throw new Error("Team ID is required");
      return getTeamBalanceAPI(teamId);
    },
    staleTime: 1000 * 20,
  })

  const { data: teamPayDetail, isSuccess: payDetailSuccess } = useQuery({
    queryKey: ["teamPayDetail", teamId],
    queryFn: async () => {
      if (!teamId) throw new Error("Team ID is required");
      return getTeamPayDetailAPI(teamId);
    },
    staleTime: 1000 * 20,
  })

  const { data: teamPayerDetail } = useQuery({

    queryKey: ["teamPayerDetail", teamId, round],
    queryFn: async () => {
      if (!teamId || !round) throw new Error("Team ID or Round is required");
      return getTeamPayerDetailsAPI({teamId, round});
    },
    staleTime: 1000 *20,
  })
  
  const { data: teamNonPayerDetail } = useQuery({

    queryKey: ["teamNonPayerDetail", teamId, round],
    queryFn: async () => {
      if (!teamId || !round) throw new Error("Team ID or Round is required");
      return getTeamNonPayerDetailsAPI({teamId, round});
    },
    staleTime: 1000 *20,
  })
  
  const { data: getMyPayerCheck } = useQuery({

    queryKey: ["getMyPayerCheck", teamId, round],
    queryFn: async () => {
      if (!teamId || !round) throw new Error("Team ID or Round is required");
      return getMyPayerCheckAPI({teamId, round});
    },
    staleTime: 1000 *20,
  }) 

  const teamMileageData:MileageStatusProps[] = useMemo(() => {
      if(!payDetailSuccess || !balanceSuccess){
        
        return [];
      }

      if(teamBalance == null){
        
        return [{type: "balance", amount: NaN}, {type: "expense", amount: NaN}];
      }
  
      const balanceData = {type:"balance" as const, amount:teamBalance};
      const expenseData = {type:"expense" as const, amount: teamPayDetail.reduce((acc, cur) => acc + cur.amount, 0)}
     
  
      return [balanceData, expenseData]
  
    },[teamBalance, teamPayDetail, payDetailSuccess, balanceSuccess])

    const teamMileageHistoryData = useMemo<RawDataRow[]>(() => {
      if (!teamPayDetail) {
          return [];
      }

      return teamPayDetail.map((data, index) => {
          let transaction = '';

          if (data.transactionCategory === 'CHARGE') {
              transaction = '충전';
          } else if (data.transactionCategory === 'TRANSFER') {
              transaction = '송금';
          } else if (data.transactionCategory === 'DEPOSIT') {
              transaction = '입금';
          } else if (data.transactionCategory === 'PAYMENT') {
              transaction = '지출';
          }

          return {
              id: index,
              transaction,
              user: "",
              name: data.memo,
              date: data.createdAt,
              amount: data.amount,
          };
      });
  }, [teamPayDetail]);

  const teamPayerHistoryData = useMemo<RawDataRow[]>(() => {
    if (!teamPayerDetail) {
        return [];
    }

    return teamPayerDetail.map((data, index) => {
        let transaction = '';

        if (data.transactionCategory === 'CHARGE') {
            transaction = '충전';
        } else if (data.transactionCategory === 'TRANSFER') {
            transaction = '송금';
        } else if (data.transactionCategory === 'DEPOSIT') {
            transaction = '입금';
        } else if (data.transactionCategory === 'PAYMENT') {
            transaction = '지출';
        }

        return {
            id: index,
            transaction,
            user: "",
            name: data.memo,
            date: data.createdAt,
            amount: data.amount,
        };
    });
}, [teamPayDetail]);

  
  return {teamMileageData, teamBalance, teamPayDetail, teamPayerDetail, teamNonPayerDetail, getMyPayerCheck, teamMileageHistoryData}
}

export default useTeamMileage;