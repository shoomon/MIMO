import { ChargePaymentRequest } from '@/types/Payment';
import { customFetch } from './customFetch';

export const chargePaymentAPI = async (
    data: ChargePaymentRequest,
): Promise<boolean> => {
    try {
        const response = await customFetch('/balance/charge', {
            method: 'POST',
            body: JSON.stringify(data),
        });

        if (!response.ok) {
            throw new Error();
        }

        return response.status == 200;
    } catch (error) {
        console.error(error);

        return false;
    }
};
