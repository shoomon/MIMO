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

  amount: number;
  senderAccountNumber : string;
  receiverAccountNumber : string;
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
  amount: string;
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



