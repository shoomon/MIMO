export interface TeamQRCodeRequest {
  teamId: string;
  accountNumber: string;
}

export type QRCodeResponse = string;

export interface UserQRCodeRequest {
  accountNumber: string;
}
