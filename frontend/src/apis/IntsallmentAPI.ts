import { CreateInstallmentRequest, InstallmentResponse, PayerDetailsRequest } from "@/types/Payment";
import { customFetch } from "./customFetch";

// create Installment
export const createTeamInstallmentAPI = async ({teamId, installmentRequests}:CreateInstallmentRequest): Promise<boolean> => {

  try {
      const response = await customFetch(`/installment/create?teamId=${teamId}`, {
          method: "POST",
          body: JSON.stringify(installmentRequests)
      });

      return response.ok;
  }catch(error){
      console.error(error);

      throw error;
  }
}

// payer-details
export const getTeamPayerDetailsAPI = async ({teamId, round}:PayerDetailsRequest): Promise<InstallmentResponse> => {

  try {
      const response = await customFetch(`/installment/payer-details?teamId=${teamId}&round=${round}`, {
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

// non-payer-details
export const getTeamNonPayerDetailsAPI = async ({teamId, round}:PayerDetailsRequest): Promise<InstallmentResponse> => {

  try {
      const response = await customFetch(`/installment/non-payer-details?teamId=${teamId}&round=${round}`, {
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

// check my payer
export const getMyPayerCheckAPI = async ({teamId, round}:PayerDetailsRequest): Promise<boolean> => {
  try {
      const response = await customFetch(`/installment/check?teamId=${teamId}&round=${round}`, {
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