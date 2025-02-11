interface ButtonToggleViewProps {
    value: string;
    /** 버튼이 활성 상태인지 여부 */
    active: boolean;
    /** 클릭 이벤트 핸들러 (파라미터 없음; container에서 value 전달 처리) */
    onClick: () => void;
    /** 버튼에 표시할 내용 (라벨) */
    children: React.ReactNode;
}

/**
 * ToggleButtonView 컴포넌트는 단일 토글 버튼의 UI를 렌더링합니다.
 */
const ButtonToggleView = ({
    active,
    onClick,
    children,
}: ButtonToggleViewProps) => {
    return (
        <button
            onClick={onClick}
            className={`h-fit w-fit justify-center rounded-sm px-[8px] py-[2px] text-lg font-bold${
                active
                    ? ' ' +
                      'outline-brand-primary-400 bg-white text-black outline-2'
                    : ' ' +
                      'text-dark border-black bg-gray-50 opacity-60 hover:bg-gray-200'
            }`}
        >
            {children}
        </button>
    );
};

export default ButtonToggleView;
