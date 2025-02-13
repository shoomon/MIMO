// MemberListView.tsx
import type { ProfileImageProps } from './../../atoms/ProfileImage/ProfileImage';
import { ButtonDefault } from '@/components/atoms';

export interface MemberListViewProps {
    parsedDate: string;
    userInfo: ProfileImageProps;
    bio: string;
    // 역할에 따른 아이콘 및 라벨 요소 (ex. 모임장, 운영진, 멤버)
    userRoleElement: React.ReactNode;
    // 멤버 관련 액션 핸들러 (예: 권한 수정, 멤버 추방)
    onEditRole: () => void;
    onKickMember: () => void;
}

const MemberListView = ({
    parsedDate,
    userInfo,
    bio,
    userRoleElement,
    onEditRole,
    onKickMember,
}: MemberListViewProps) => {
    return (
        <div className="flex items-end justify-between">
            <div className="flex items-center gap-3">
                <div className="flex h-[84px] w-[84px] flex-shrink-0 items-center justify-center overflow-hidden rounded-2xl">
                    <img
                        src={userInfo.profileUri}
                        alt="프로필 이미지"
                        className="h-full w-full object-cover"
                    />
                </div>
                <div className="flex flex-col gap-1">
                    <div className="flex items-center gap-1">
                        {userRoleElement}
                        <span className="text-lg font-medium">
                            {userInfo.nickname}
                        </span>
                    </div>
                    <span className="text-md font-medium">{bio}</span>
                    <span className="text-md font-normal">
                        가입일 {parsedDate}
                    </span>
                </div>
            </div>
            <div className="flex gap-3">
                <ButtonDefault
                    type="default"
                    content="권한 수정"
                    onClick={onEditRole}
                />
                <ButtonDefault
                    type="fail"
                    content="멤버 추방"
                    onClick={onKickMember}
                />
            </div>
        </div>
    );
};

export default MemberListView;
