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

export type BalanceResponse = number;

export interface AccountDetailsResponse {

  amount: number; // 금액
  senderAccountNumber : string; // 돈이 나가는 계좌
  receiverAccountNumber : string; // 돈이 들어오는 계좌
  senderNickname: string; // 돈 쓴 사람
  receiverNickname: string; // 돈 받은 사람람
  memo: string;
  transactionCategory : 'CHARGE' | 'TRANSFER' | 'DEPOSIT' | 'PAYMENT';
  createdAt: string;

}

export interface TransferRequest {
  amount: string;
  receiverAccountNumber: string;
}

export interface InstallmentRequest {
  teamId: string;
  userId: string;
  round: string;
  amount: string;
  transferRequest: TransferRequest;
}

export interface InstallmentResponse {
  nickname: string;
  amount: number;
  installmentDate: string;
}

export interface CreateInstallmentRequest {
  teamId: string;
  installmentRequests: InstallmentRequest[];
}

export interface PayerDetailsRequest {
  teamId: string;
  round: string;
}



