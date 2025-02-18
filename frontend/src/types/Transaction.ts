export interface ChargeRequest {
  amount: string;
  impUid: string;
  merchantUid: string;
}

export interface TransferRequest{
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

export interface PaymentRequest {
  uuid: string;
  amount: string;
  receiverAccountNumber: string;
  memo: string;
}