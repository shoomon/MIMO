import { QRCodeResponse, TeamQRCodeRequest, UserQRCodeRequest } from "@/types/QRCode";
import { customFetch } from "./customFetch";

// QRCode-user
export const getUserQRCodeAPI = async ({accountNumber}:UserQRCodeRequest): Promise<QRCodeResponse> => {

  try {
      const response = await customFetch(`/qrcode/user?accountNumber=${accountNumber}`, {
          method: "GET",
      });

      if(!response.ok){
          throw new Error("데이터 가져오는 중 오류");
      }

      return response.json();
  }catch(error){
      console.error(error);

      throw error;
  }
}

// QRcode-team
export const getTeamQRCodeAPI = async ({accountNumber, teamId}:TeamQRCodeRequest): Promise<QRCodeResponse> => {

 console.log("QR 코드 함수 호출");

  try {
      const response = await customFetch(`/qrcode/team?teamId=${teamId}&accountNumber=${accountNumber}`, {
          method: "GET",
      });

      if(!response.ok){
          throw new Error("데이터 가져오는 중 오류");
      }

      return response.text();
  }catch(error){
      console.error(error);

      throw error;
  }
}