import { getTeamBalanceAPI, getTeamPayDetailAPI } from "@/apis/AccountAPI";
import { getMyPayerCheckAPI, getTeamCurrentRoundAPI, getTeamNonPayerDetailsAPI, getTeamPayerDetailsAPI } from "@/apis/IntsallmentAPI";
import { getTeamUsers } from "@/apis/TeamAPI";
import { MileageStatusProps } from "@/components/atoms/MileageStatus/MileageStatus";
import { RawDataRow } from "@/utils/transformTableData";
import { useQuery } from "@tanstack/react-query";
import { useMemo } from "react";
import useMyTeamProfile from "./useMyTeamProfile";

const useTeamMileage = (teamId:string, round:string) => {

  const { data : teamInfo } = useQuery({
    queryKey: ["teamInfo", teamId],
    queryFn: async () => {
      if(!teamId) throw new Error("Team ID is required");
      return getTeamUsers(teamId);
    },
    staleTime: 1000 * 20
  })

  const { data : myTeamInfo } = useMyTeamProfile(teamId);

  const { data : myPayCheck } = useQuery({
    queryKey: ["payCheck", teamId],
    queryFn: async () => {
      if(!teamId || !round) throw new Error("Team ID or Round is required");
      return getMyPayerCheckAPI({teamId, round});
    },
    staleTime: 1000 * 20
  })

  const { data : teamCurrentRound } = useQuery({
    queryKey: ["teamCurrentRound", teamId],
    queryFn: async () => {
      if(!teamId) throw new Error("Team ID is required");

      return getTeamCurrentRoundAPI({teamId});
    },
    staleTime: 1000 * 5
  })

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

  const teamPayerHistoryShortData = useMemo<RawDataRow[]>(() => {
    if (!teamPayerDetail) {
        return [];
    }

    const data = teamPayerDetail.map((data, index) => {

        return {
            id: index,
            transaction: "납부",
            user: data.nickname,
            name: "",
            date: data.installmentDate,
            amount: data.amount,
        };
    });

    if(data.length > 5){
      return data.slice(0, 5);
    }else{
      return data;
    }

  }, [teamPayerDetail]);

  const teamPayerHistoryData = useMemo<RawDataRow[]>(() => {
      if (!teamPayerDetail) {
          return [];
      }
  
      return teamPayerDetail.map((data, index) => {
  
          return {
              id: index,
              transaction: "납부",
              user: data.nickname,
              name: "",
              date: data.installmentDate,
              amount: data.amount,
          };
      });
  
  }, [teamPayerDetail]);    

  const teamNonPayerHistoryShortData = useMemo<RawDataRow[]>(() => {
    if (!teamNonPayerDetail) {
      return [];
    }

    const data =  teamNonPayerDetail.map((data, index) => {

      return {
          id: index,
          transaction: "미납부",
          user: data.nickname,
          name: "",
          date: data.installmentDate,
          amount: data.amount,
      };
    });

    if(data.length > 5){
      return data.slice(0, 5);
    }else{
      return data;
    }

  }, [teamNonPayerDetail]);

  const teamNonPayerHistoryData = useMemo<RawDataRow[]>(() => {
    if (!teamNonPayerDetail) {
      return [];
    }

    return teamNonPayerDetail.map((data, index) => {

      return {
          id: index,
          transaction: "미납부",
          user: data.nickname,
          name: "",
          date: data.installmentDate,
          amount: data.amount,
      };
    });

  }, [teamNonPayerDetail]);
  
  return {teamCurrentRound, teamInfo, myTeamInfo, myPayCheck, teamMileageData, teamPayerHistoryData, teamPayerHistoryShortData, teamNonPayerHistoryData, teamNonPayerHistoryShortData, teamBalance, teamPayDetail, teamPayerDetail, teamNonPayerDetail, getMyPayerCheck, teamMileageHistoryData}
}

export default useTeamMileage;