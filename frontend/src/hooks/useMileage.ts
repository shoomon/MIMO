import { getUserAllDetailsAPI, getUserChargeDetailAPI, getUserDepositDetailAPI, getUserPayBalanceAPI, getUserPayDetailsAPI, getUserTransferDetailAPI } from "@/apis/AccountAPI";
import { useQuery } from "@tanstack/react-query";
import { useAuth } from "./useAuth";

// Balance : 잔액
// Deposit : 입금
// Charge : 충전
// Transfer : 송금
// Pay : 지출
// All : 전체 내역


const useMyMileage = () => {

  const { userInfo } = useAuth();

  // 잔액
  const { data : myBalance } = useQuery({
    queryKey: ["myBalance", userInfo],
    queryFn: getUserPayBalanceAPI,
    staleTime: 1000 * 20,
  })

  // 입금 내역
  const { data : myDepositList } = useQuery({
    queryKey: ["myDepositList", userInfo],
    queryFn: getUserDepositDetailAPI,
    staleTime: 1000 * 20,
  })

  // 충전 내역
  const { data : myChargeList } = useQuery({
    queryKey: ["myChargeList", userInfo],
    queryFn: getUserChargeDetailAPI,
    staleTime: 1000 * 20,
  })
  
  // 송금 내역
  const { data : myTransferList } = useQuery({
    queryKey: ["myTransferList", userInfo],
    queryFn: getUserTransferDetailAPI,
    staleTime: 1000 * 20,
  })

  // 지출 내역
  const { data : myPayList } = useQuery({
    queryKey: ["myPayList", userInfo],
    queryFn : getUserPayDetailsAPI,
    staleTime: 1000 * 20,
  })

  // 전체체 내역
  const { data : myAllList } = useQuery({
    queryKey: ["myAllList", userInfo],
    queryFn: getUserAllDetailsAPI,
    staleTime: 1000 * 20,
  })

  return {myBalance, myDepositList, myChargeList, myTransferList, myPayList, myAllList}
}

export default useMyMileage;