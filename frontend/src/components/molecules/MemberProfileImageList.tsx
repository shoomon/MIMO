import { ProfileImageProps } from '../atoms/ProfileImage/ProfileImage';
import { Link } from 'react-router-dom';
import ProfileImage from '../atoms/ProfileImage/ProfileImage';

const LIMIT_RENDER = 5;

/**
 * 프로필 이미지를 렌더링하는 유틸 함수
 * @param memberList - 프로필 이미지 목록
 * @param link - 추가 멤버를 표시할 경우 이동할 링크 (기본값: "/member-list")
 * @returns React.ReactNode
 */
export const renderMemberProfiles = (
    memberList: ProfileImageProps[],
    link: string = '/member-list',
): React.ReactNode => {
    if (memberList.length === 0) return null; // 멤버가 없을 경우 렌더링 안함

    if (memberList.length <= LIMIT_RENDER) {
        return memberList.map((member) => (
            <ProfileImage
                key={member.userId}
                userId={member.userId}
                profileUri={member.profileUri}
                nickname={member.nickname}
                size={48}
                addStyle="rounded-lg"
            />
        ));
    }

    // 5명 초과 시 첫 4명만 렌더링하고, 나머지는 +숫자로 표시
    const firstFour = memberList.slice(0, 4);
    const remainingCount = memberList.length - 4;

    return (
        <>
            {firstFour.map((member) => (
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
                to={link}
                className="text-md flex h-[48px] w-[48px] items-center justify-center rounded-lg bg-gray-700 font-semibold text-white"
            >
                +{remainingCount}
            </Link>
        </>
    );
};
