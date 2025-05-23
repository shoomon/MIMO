// CardScheduleView.tsx
import React from 'react';
import { Link } from 'react-router-dom';

export interface CardScheduleViewProps {
    // Card 상단 헤더 (신청 마감까지 남은 시간)
    timeLeftStr: string;
    // 일정 카드에 표시할 제목
    label: string;
    // 일정 날짜 (포맷팅 된 문자열)
    formattedDate: string;
    // 참가비 문자열 (예: "참가비 1000원")
    feeText: string;
    // 참가 멤버 렌더링 요소 (프로필 이미지 리스트 혹은 링크 포함)
    memberProfiles: React.ReactNode;
    // 카드 클릭 시 이동할 링크 (일정 상세 페이지)
    detailLink: string;
    //종료된 일정인가?
    isClosed: boolean;
}

const CardScheduleView: React.FC<CardScheduleViewProps> = ({
    timeLeftStr,
    label,
    formattedDate,
    feeText,
    memberProfiles,
    detailLink,
    isClosed,
}) => {
    return (
        <div className="h-fit pb-4">
            <Link
                to={detailLink}
                className={`flex w-[344px] flex-col justify-between overflow-hidden rounded-lg pb-3 ${isClosed ? 'bg-gray-100 opacity-70' : 'bg-white'}`}
            >
                <div
                    className={`text-display-xs flex w-full justify-center gap-1 px-4 py-3 font-bold ${isClosed ? 'text-dark bg-gray-200' : 'bg-gray-700 text-white'}`}
                >
                    {isClosed ? (
                        '신청마감'
                    ) : (
                        <>
                            <span className="text-display-xs font-semibold">
                                신청 마감까지
                            </span>
                            {timeLeftStr}
                        </>
                    )}
                </div>
                <div className="flex h-full w-full flex-col gap-4 px-4 py-3">
                    <div className="flex h-[118px] flex-col gap-2">
                        <span className="text-dark h-full text-xl font-bold">
                            {label}
                        </span>
                        <div className="text-md text-text flex h-fit flex-col gap-[2px] font-semibold">
                            <span>{formattedDate}</span>
                            <span>{feeText}</span>
                        </div>
                    </div>
                </div>
            </Link>
            <div className="flex flex-col gap-1 px-4">
                <span className="text-md text-dark font-medium">참가 멤버</span>
                <div className="flex gap-[18px]">{memberProfiles}</div>
            </div>
        </div>
    );
};

export default CardScheduleView;
