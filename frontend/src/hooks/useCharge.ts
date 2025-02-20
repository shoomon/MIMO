import { getMyAllInfoAPI } from "@/apis/AuthAPI";
import { chargePaymentAPI } from "@/apis/TransactionAPI";
import { Iamport } from "@/types/Payment";
import { ChargeRequest } from "@/types/Transaction";
import generateOrderUid from "@/utils/generateOrderUid";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useCallback, useEffect, useState } from "react";
import { useAuth } from "./useAuth";

declare global {
  interface Window {
    IMP?: Iamport;
  }
}

const useCharge = () => {

  const { IMP } = window;
  const [payment, setPayment] = useState<number>(0);
  const [isOpen, setOpen] = useState<boolean>(false);
  const [isImpLoaded, setIsImpLoaded] = useState(false);
  const { userInfo } = useAuth();
  const queryClient = useQueryClient();


  const { data: userAllInfo } = useQuery({
    queryKey: ['myAllData'],
    queryFn: getMyAllInfoAPI,
    staleTime: 1000 * 30,
    gcTime: 1000 * 60,
  });

  const mutationMileage = useMutation({
    mutationFn:(data: ChargeRequest) => {
                return chargePaymentAPI(data);
    },
    onSuccess: (data) => {
      if(!data) return;

      queryClient.invalidateQueries({ queryKey : ["myBalance", userInfo]});
      // 마일리지 쿼리 연결
    }
  })

  useEffect(() => {
    const iamport = document.createElement('script');
    iamport.src = 'https://cdn.iamport.kr/v1/iamport.js';

    iamport.onload = () => {
      setIsImpLoaded(true);
    }
        
    document.head.appendChild(iamport);

        return () => {
            document.head.removeChild(iamport);
        };
  },[])

  const chargePayment = useCallback(() => {

    if(!isImpLoaded || !userAllInfo || !IMP){

      return;
    }

    IMP.init(import.meta.env.VITE_IMP_KEY);

    const orderUid = generateOrderUid();
    const itemName = 'MIMO 마일리지 충전';
    const paymentPrice = payment;
    const buyerName = userAllInfo.nickname;
    const buyerEmail = userAllInfo.email;
    const buyerAddress = '역삼동 뺑뺑이 ssafy';

    IMP.request_pay(
      {
          pg: 'html5_inicis.INIpayTest',
          pay_method: 'card',
          merchant_uid: orderUid, // 주문 번호
          name: itemName, // 상품 이름
          amount: paymentPrice, // 상품 가격
          buyer_email: buyerEmail, // 구매자 이메일
          buyer_name: buyerName, // 구매자 이름
          buyer_tel: '010-1234-5678', // 임의의 값
          buyer_addr: buyerAddress, // 구매자 주소
          buyer_postcode: '123-456', // 임의의 값
      },
      async function (rsp) {
          if (rsp.success) {
              const chargeData = {
                  amount: paymentPrice,
                  impUid: rsp.imp_uid,
                  merchantUid: rsp.merchant_uid,
              };

              try {
                  mutationMileage.mutate(chargeData);
              } catch (error) {
                  console.error('충전 실패...', error);
              }
          } else {

          alert(`결제에 실패했습니다`);
          }
      },
    );
  }, [IMP, isImpLoaded, mutationMileage, payment, userAllInfo])

  const handleConfirm = async (value: string) => {

    if(Number.isNaN(Number(value))) {
      // 모달로 알려줘야함함
      return;
    }

    setPayment(Number(value));
    
    setOpen(false);
  }

  useEffect(() => {
    if(payment > 0){
      chargePayment();
      setPayment(0);
    }
  }, [payment, chargePayment])

  const handleCancel = () => {
    setOpen(false);
  }

  const handleCharge = () => {
    setOpen(true);
  }

  return { isOpen, handleConfirm, handleCharge, handleCancel }
}

export default useCharge;