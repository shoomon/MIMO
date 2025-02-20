import { AccountDetailsResponse, BalanceResponse } from '@/types/Payment';
import { customFetch } from './customFetch';

// userDepositDetails *
export const getUserDepositDetailAPI = async (): Promise<
    AccountDetailsResponse[]
> => {
    try {
        const response = await customFetch('/account/user/deposit-details', {
            method: 'GET',
        });

        if (!response.ok) {
            throw new Error('데이터 가져오는 중 오류');
        }

        return response.json();
    } catch (error) {
        console.error(error);

        throw error;
    }
};

// userChargeDetails *
export const getUserChargeDetailAPI = async (): Promise<
    AccountDetailsResponse[]
> => {
    try {
        const response = await customFetch('/account/user/charge-details', {
            method: 'GET',
        });

        if (!response.ok) {
            throw new Error('데이터 가져오는 중 오류');
        }

        return response.json();
    } catch (error) {
        console.error(error);

        throw error;
    }
};

// userTransferDetails *
export const getUserTransferDetailAPI = async (): Promise<
    AccountDetailsResponse[]
> => {
    try {
        const response = await customFetch('/account/user/transfer-details', {
            method: 'GET',
        });

        if (!response.ok) {
            throw new Error('데이터 가져오는 중 오류');
        }

        return response.json();
    } catch (error) {
        console.error(error);

        throw error;
    }
};

// userPayDetails *
export const getUserPayDetailsAPI = async (): Promise<
    AccountDetailsResponse[]
> => {
    try {
        const response = await customFetch('/account/user/pay-details', {
            method: 'GET',
        });

        if (!response.ok) {
            throw new Error('데이터 가져오는 중 오류');
        }

        return response.json();
    } catch (error) {
        console.error(error);

        throw error;
    }
};

// userAllDetails *
export const getUserAllDetailsAPI = async (): Promise<
    AccountDetailsResponse[]
> => {
    try {
        const response = await customFetch('/account/user/all-details', {
            method: 'GET',
        });

        if (!response.ok) {
            throw new Error('데이터 가져오는 중 오류');
        }

        return response.json();
    } catch (error) {
        console.error(error);

        throw error;
    }
};

// userBalance *
export const getUserPayBalanceAPI = async (): Promise<BalanceResponse> => {
    try {
        const response = await customFetch('/account/user/balance', {
            method: 'GET',
        });

        if (!response.ok) {
            throw new Error('데이터 가져오는 중 오류');
        }

        return response.json();
    } catch (error) {
        console.error(error);

        throw error;
    }
};

// teamBalance *
export const getTeamBalanceAPI = async (
    teamId: string,
): Promise<BalanceResponse> => {
    try {
        const response = await customFetch(
            `/account/team/balance?teamId=${teamId}`,
            {
                method: 'GET',
            },
        );

        if (!response.ok) {
            throw new Error('데이터 가져오는 중 오류');
        }

        return response.json();
    } catch (error) {
        console.error(error);

        throw error;
    }
};

// teamPayDetails *
export const getTeamPayDetailAPI = async (
    teamId: string,
): Promise<AccountDetailsResponse[]> => {
    try {
        const response = await customFetch(
            `/account/team/pay-details?teamId=${teamId}`,
            {
                method: 'GET',
            },
        );

        if (!response.ok) {
            throw new Error('데이터 가져오는 중 오류');
        }

        return response.json();
    } catch (error) {
        console.error(error);

        throw error;
    }
};
