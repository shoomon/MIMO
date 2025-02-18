import BasicInputModal from '@/components/molecules/BasicInputModal/BasicInputModal';
import useCharge from '@/hooks/useCharge';

const MyPage = () => {
    const { isOpen, handleConfirm, handleCharge, handleCancel } = useCharge();

    return (
        <div>
            <h1>마이페이지</h1>

            <button
                type="button"
                className="bg-brand-primary-300 cursor-pointer p-2 text-white"
                onClick={handleCharge}
            >
                충전하기
            </button>
            <BasicInputModal
                isOpen={isOpen}
                title="충전 금액을 입력해주세요."
                subTitle="100원 이상부터 충전 가능합니다."
                inputPlaceholder="금액을 입력하세요."
                confirmLabel="충전하기"
                onConfirmClick={handleConfirm}
                onCancelClick={handleCancel}
            />
        </div>
    );
};

export default MyPage;
