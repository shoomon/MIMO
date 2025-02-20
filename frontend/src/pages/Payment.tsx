import { payPaymentAPI } from '@/apis/TransactionAPI';
import { useEffect } from 'react';
import { useParams } from 'react-router-dom';

const Payment = () => {
    const { uuid, amount, receiverAccountNumber, memo } = useParams();

    useEffect(() => {
        if (!uuid || !amount || !receiverAccountNumber || !memo) {
            return;
        }

        const payment = {
            uuid,
            amount,
            receiverAccountNumber,
            memo,
        };

        const fetchData = async () => {
            const data = await payPaymentAPI(payment);

            if (data) {
                alert('결제성공!');
            }
        };

        fetchData();
    }, []);

    return null;
};

export default Payment;
