import Icon from '../Icon/Icon';

interface MemberCountProps {
    /**
     * 최대 멤버 수를 나타내는 숫자입니다.
     */
    memberLimit: number;

    /**
     * 현재 멤버 수를 나타내는 숫자자입니다.
     */
    memberCount: number;

    /**
     * 버튼 클릭 시 실행할 함수입니다.
     *
     * @param {React.MouseEvent<HTMLButtonElement>} e - 클릭 이벤트 객체
     */
    onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
    addStyle: string;
}

/**
 * `MemberCount` 컴포넌트는 현재 멤버 수와 최대 멤버 수를 표시하는 버튼을 렌더링합니다.
 *
 * @component
 * @param {number} memberLimit - 최대 멤버 수입니다.
 * @param {number} memberCount - 현재 멤버 수입니다.
 * @param {(e: React.MouseEvent<HTMLButtonElement>) => void} [onClick] - 클릭 이벤트 핸들러입니다.
 * @returns {JSX.Element} - 렌더링된 `MemberCount` 컴포넌트를 반환합니다.
 */
const MemberCount = ({
    memberLimit,
    memberCount,
    onClick,
    addStyle,
}: MemberCountProps): JSX.Element => {
    return (
        <button
            onClick={onClick}
            className={`hover:outline-brand-primary-400 h-fit w-fit rounded-sm border-1 border-gray-500 bg-white px-2 py-1 hover:outline-2 ${addStyle}`}
        >
            <div className="flex gap-1">
                <Icon type="svg" id="User" />
                <div className="flex items-center text-sm font-semibold text-gray-700">
                    {memberCount}/{memberLimit}
                </div>
            </div>
        </button>
    );
};

export default MemberCount;
