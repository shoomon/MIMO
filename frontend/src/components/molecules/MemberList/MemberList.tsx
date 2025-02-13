// MemberList.tsx
import { dateParsing } from '@/utils';
import type { ProfileImageProps } from './../../atoms/ProfileImage/ProfileImage';
import { Icon } from '@/components/atoms';
import MemberListView from './MemberList.view';
import { TeamUserRole } from '@/types/Team';
import { deleteUsers } from '@/apis/TeamAPI';
import { useParams } from 'react-router-dom';

/**
 * MemberList 컴포넌트의 props 타입 정의
 */
export interface MemberListProps {
    /** 사용자의 역할 ('LEADER' | 'CO_LEADER' | 'MEMBER') */
    role: TeamUserRole;
    /** 사용자의 프로필 정보 */
    userInfo: ProfileImageProps;
    /** 사용자 소개 (한 줄 소개) */
    bio: string;
    /** 가입 날짜 (ISO 8601 형식) */
    joinDate: string;
}

/**
 * 모임 멤버 리스트의 개별 멤버 항목을 표시하는 컴포넌트
 *
 * @param {MemberListProps} props - 컴포넌트 props
 * @param {ProfileImageProps} props.userInfo - 사용자의 프로필 정보
 * @param {string} props.bio - 사용자 소개 (한 줄 소개)
 * @param {string} props.joinDate - 가입 날짜 (ISO 8601 형식)
 *
 * @returns {JSX.Element} 멤버 정보를 렌더링하는 React 컴포넌트
 */
const MemberList = ({
    role,
    userInfo,
    bio,
    joinDate,
}: MemberListProps): JSX.Element => {
    /** 날짜를 포맷팅하여 변환 (두 번째 인자가 true인 경우 포맷 옵션 적용) */
    const parsedDate = dateParsing(new Date(joinDate), true);
    const { teamId } = useParams<{ teamId: string }>();

    if (!teamId) {
        return <div>teamId가 없습니다.</div>;
    }

    /**
     * 역할에 따른 아이콘 및 라벨을 렌더링하는 함수
     *
     * @param {string} role - 사용자의 역할 ('LEADER', 'CO_LEADER', 'MEMBER')
     * @returns {JSX.Element} 역할을 나타내는 UI 요소
     */
    const renderUserRoleElement = (role: string): JSX.Element => {
        if (role === 'LEADER') {
            return (
                <div className="flex items-center gap-1">
                    <Icon type="svg" id="Crown" className="mb-1" />
                    <span className="flex items-center text-lg font-extrabold">
                        모임장
                    </span>
                </div>
            );
        }
        if (role === 'CO_LEADER') {
            return (
                <div className="flex items-center gap-1">
                    <Icon type="svg" id="Medal" />
                    <span className="flex items-center text-lg font-extrabold">
                        운영진
                    </span>
                </div>
            );
        }
        return <span className="text-lg font-extrabold">멤버</span>;
    };

    /**
     * 권한 수정 버튼 클릭 핸들러
     * (추후 권한 수정 기능 추가 예정)
     */
    const handleEditRole = (): void => {
        // TODO: 권한 수정 로직 구현
    };

    /**
     * 멤버 추방 버튼 클릭 핸들러
     * (추후 멤버 추방 기능 추가 예정)
     */
    const handleKickMember = (): void => {
        if (!userInfo.userId) {
            return;
        }
        deleteUsers(teamId, userInfo.userId);
    };

    return (
        <MemberListView
            parsedDate={parsedDate}
            userInfo={userInfo}
            bio={bio}
            userRoleElement={renderUserRoleElement(role)}
            onEditRole={handleEditRole}
            onKickMember={handleKickMember}
        />
    );
};

export default MemberList;
