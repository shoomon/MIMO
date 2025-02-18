export interface RequestPayParmas {
  pg: string,
  pay_method: string,
  merchant_uid: string;
  name: string;
  amount: number;
  buyer_email: string;
  buyer_name: string;
  buyer_tel: string;
  buyer_addr: string;
  buyer_postcode: string;
}

export interface ChargePaymentRequest {
  amount: number;
  impUid: string;
  merchantUid: string;
}

export interface IamportResponseProps {
  amount: number;
  imp_uid: string;
  merchant_uid: string;
  success: boolean;
}

export type RequestPayResponseCallback = (rsp: IamportResponseProps) => void;

export interface Iamport {
    init: (accountId: string) => void;
    request_pay: (
        params: RequestPayParmas,
        callback?: RequestPayResponseCallback,
    ) => void;
}