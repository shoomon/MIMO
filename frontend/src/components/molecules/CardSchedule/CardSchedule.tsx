// CardSchedule.tsx
import React from 'react';
import CardScheduleView from './CardSchedule.View';
import { ProfileImageProps } from '@/components/atoms/ProfileImage/ProfileImage';
import { dateParsing } from '@/utils';
import { renderMemberProfiles } from '@/utils/memberParsing';

/**
 * CardSchedule 컴포넌트의 props 타입 정의
 */
export interface CardScheduleProps {
    /** 참여 멤버 리스트 */
    memberList: ProfileImageProps[];
    /** 일정이 예정된 날짜 및 시간 (ISO 8601 형식) */
    scheduledDateTime: string;
    /** 일정의 제목 또는 라벨 */
    label: string;
    /** 참가비 (문자열, 원 단위) */
    entryFee: number;
    /**종료된 일정 */
    isClosed: boolean;
    /** 일정 상세 페이지 링크 */
    detailLink: string;
}

/**
 * 일정 카드 컴포넌트
 *
 * @param {CardScheduleProps} props - 컴포넌트 props
 * @param {string} props.scheduledDateTime - 일정이 예정된 날짜 및 시간
 * @param {string} props.label - 일정의 제목 또는 라벨
 * @param {string} props.entryFee - 참가비 (원 단위)
 * @param {ProfileImageProps[]} props.memberList - 참가 멤버 리스트
 *
 * @returns {JSX.Element} 일정 정보를 표시하는 카드 UI
 */
const CardSchedule: React.FC<CardScheduleProps> = ({
    scheduledDateTime,
    label,
    entryFee,
    memberList,
    isClosed = false,
    detailLink,
}) => {
    /** 일정까지 남은 시간을 계산 */
    const targetDate = new Date(scheduledDateTime);
    const now = new Date();
    const diffMs = targetDate.getTime() - now.getTime();
    const oneDayMs = 24 * 60 * 60 * 1000;

    let timeLeftStr = '';
    if (diffMs < 0) {
        isClosed = true;
    } else if (diffMs < oneDayMs) {
        // 1일 미만이면 시간과 분을 표시
        const hours = Math.floor(diffMs / (60 * 60 * 1000));
        const minutes = Math.floor((diffMs % (60 * 60 * 1000)) / (60 * 1000));
        timeLeftStr = `${hours}시간 ${minutes}분`;
    } else {
        // 1일 이상이면 남은 일수를 D-형식으로 표시
        const days = Math.floor(diffMs / oneDayMs);
        timeLeftStr = `D-${days}`;
    }

    /** 참가비 텍스트 */
    const feeText = `참가비 ${entryFee}원`;
    /** 날짜 포맷팅 */
    const formattedDate = dateParsing(targetDate);

    const safeMemberList = memberList ?? [];

    const memberProfiles = renderMemberProfiles(safeMemberList);
    /** 프로필 이미지 렌더링 */

    return (
        <CardScheduleView
            timeLeftStr={timeLeftStr}
            label={label}
            formattedDate={formattedDate}
            feeText={feeText}
            memberProfiles={memberProfiles}
            detailLink={detailLink}
            isClosed={isClosed}
        />
    );
};

export default CardSchedule;
