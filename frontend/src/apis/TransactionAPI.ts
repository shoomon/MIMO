import { ChargeRequest, TransferRequest } from "@/types/Transaction";
import { customFetch } from "./customFetch";
import { InstallmentRequest } from "@/types/Payment";

export const chargePaymentAPI = async (
    data: ChargeRequest,
): Promise<boolean> => {
    try {
        const response = await customFetch('/balance/charge', {
            method: 'POST',
            body: JSON.stringify(data),
        });

        if (!response.ok) {
            throw new Error();
        }

        return response.ok;
    } catch (error) {
        console.error(error);

        return false;
    }
};

export const transferPaymentAPI = async (data: TransferRequest): Promise<boolean> => {

  try {
    const response = await customFetch('/balance/transfer', {
        method: 'POST',
        body: JSON.stringify(data),
    });

    if (!response.ok) {
        throw new Error();
    }

    return response.ok;
  } catch (error) {
    console.error(error);

    return false;
  }
}

export const installmentPaymentAPI = async (data: InstallmentRequest): Promise<boolean> => {

  try {
    const response = await customFetch('/balance/installment', {
        method: 'POST',
        body: JSON.stringify(data),
    });

    if (!response.ok) {
        throw new Error();
    }

    return response.ok;
  } catch (error) {
    console.error(error);

    return false;
  }
}

export const payPaymentAPI = async (data: PaymentRequest): Promise<boolean> => {

  try {
    const response = await customFetch('/balance/pay', {
        method: 'POST',
        body: JSON.stringify(data),
    });

    if (!response.ok) {
        throw new Error();
    }

    return response.ok;
  } catch (error) {
    console.error(error);

    return false;
  }
}