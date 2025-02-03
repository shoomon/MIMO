import Icon from '../Icon/Icon';

interface MemberCountProps {
    /**
     * 최대 멤버 수를 나타내는 문자열입니다.
     */
    memberLimit: string;

    /**
     * 현재 멤버 수를 나타내는 문자열입니다.
     */
    memberCount: string;

    /**
     * 버튼 클릭 시 실행할 함수입니다.
     *
     * @param {React.MouseEvent<HTMLButtonElement>} e - 클릭 이벤트 객체
     */
    onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

/**
 * `MemberCount` 컴포넌트는 현재 멤버 수와 최대 멤버 수를 표시하는 버튼을 렌더링합니다.
 *
 * @component
 * @param {string} memberLimit - 최대 멤버 수입니다.
 * @param {string} memberCount - 현재 멤버 수입니다.
 * @param {(e: React.MouseEvent<HTMLButtonElement>) => void} [onClick] - 클릭 이벤트 핸들러입니다.
 * @returns {JSX.Element} - 렌더링된 `MemberCount` 컴포넌트를 반환합니다.
 */
const MemberCount = ({
    memberLimit,
    memberCount,
    onClick,
}: MemberCountProps): JSX.Element => {
    return (
        <button
            onClick={onClick}
            className="h-fit w-fit rounded-sm border-1 border-gray-500 px-2 py-1"
        >
            <div className="flex gap-1">
                <Icon type="svg" id="User" />
                <div className="flex justify-center text-sm font-semibold text-gray-700">
                    {memberCount}/{memberLimit}
                </div>
            </div>
        </button>
    );
};

export default MemberCount;
