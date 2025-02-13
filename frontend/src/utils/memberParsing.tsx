import ProfileImage, {
    ProfileImageProps,
} from '@/components/atoms/ProfileImage/ProfileImage';
import React from 'react';
import { Link } from 'react-router-dom';

export function renderMemberProfiles(
    memberList: ProfileImageProps[] = [], // 기본값을 빈 배열로 할당
): React.ReactNode {
    const LIMIT_RENDER = 5;
    const memberCount = memberList.length;

    if (memberCount <= LIMIT_RENDER) {
        // 멤버 수가 5명 이하일 경우 모두 렌더링
        return (
            <>
                {memberList.map((member: ProfileImageProps) => (
                    <ProfileImage
                        key={member.userId}
                        userId={member.userId}
                        profileUri={member.profileUri}
                        nickname={member.nickname}
                        size={48}
                        addStyle="rounded-lg"
                    />
                ))}
            </>
        );
    } else {
        // 5명 초과일 경우, 첫 4명만 렌더링하고 나머지는 숫자로 표시
        const firstFour = memberList.slice(0, 4);
        const remainingCount = memberList.length - 4;

        return (
            <>
                {firstFour.map((member: ProfileImageProps) => (
                    <ProfileImage
                        key={member.userId}
                        userId={member.userId}
                        profileUri={member.profileUri}
                        nickname={member.nickname}
                        size={48}
                        addStyle="rounded-lg"
                    />
                ))}
                <Link
                    to="/member-list" // 멤버 목록 페이지 링크
                    className="text-md flex h-[48px] w-[48px] items-center justify-center rounded-lg bg-gray-700 font-semibold text-white"
                >
                    +{remainingCount}
                </Link>
            </>
        );
    }
}
