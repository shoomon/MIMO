import { payPaymentAPI } from '@/apis/TransactionAPI';
import { useParams } from 'react-router-dom';

const Payment = () => {
    const { uuid, receiverAccountNumber } = useParams();
    // 상품 목록 정의
    const products = [
        { id: 1, memo: '바나나우유', amount: 10000 },
        { id: 2, memo: '양념치킨', amount: 30000 },
        { id: 3, memo: '모둠회', amount: 50000 },
        { id: 4, memo: '후라이드치킨', amount: 30000 },
        { id: 5, memo: '스타벅스', amount: 40000 },
        { id: 6, memo: '광어회', amount: 20000 },
        { id: 7, memo: '뼈해장국', amount: 10000 },
        { id: 8, memo: '감자탕 대', amount: 30000 },
        { id: 9, memo: '커피빈', amount: 20000 },
    ];

    const handlePayment = (memo: string, amount: number) => {
        const cost = String(amount);

        if (!uuid || !cost || !receiverAccountNumber || !memo) {
            return;
        }

        const payment = {
            uuid,
            amount: cost,
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
    };

    return (
        <div className="container mx-auto p-8">
            <h1 className="mb-8 text-3xl font-bold">상품 구매</h1>
            <div className="grid grid-cols-1 gap-6 md:grid-cols-3">
                {products.map((product) => (
                    <div
                        key={product.id}
                        className="rounded-lg border bg-white p-6 shadow-md transition-shadow hover:shadow-lg"
                    >
                        <h2 className="mb-4 text-xl font-semibold">
                            {product.memo}
                        </h2>
                        <p className="mb-4 text-lg">
                            {product.amount.toLocaleString()}원
                        </p>
                        <button
                            onClick={() =>
                                handlePayment(product.memo, product.amount)
                            }
                            className="w-full rounded bg-blue-500 px-4 py-2 text-white transition-colors hover:bg-blue-600"
                        >
                            구매하기
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Payment;
