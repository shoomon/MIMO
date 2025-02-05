// CardBoard.tsx
import CardBoardView from './CardBoard.view';
import { ThumbnailProps } from '../Thumbnail/Thumbnail.view';
import { dateParsing } from '@/utils';

/**
 * 게시판 카드(CardBoard) 컴포넌트의 props 타입 정의
 */
export interface CardBoardProps {
    /** 게시판 카드의 썸네일 이미지 */
    image: ThumbnailProps;
    /** 게시글 제목 또는 라벨 */
    label: string;
    /** 작성자 이름 */
    author: string;
    /** 작성 날짜 (ISO 8601 형식 문자열) */
    date: string;
    /** 조회수 */
    viewCount: number;
    /** 레이아웃 타입 ('List' | 'Card') */
    layoutType: 'List' | 'Card';
    /** 카드 클릭 시 이동할 링크 (기본값: "/") */
    linkTo?: string;
}

/**
 * 게시판 카드 컴포넌트
 *
 * @param {CardBoardProps} props - 컴포넌트 props
 * @param {ThumbnailProps} props.image - 게시판 카드의 썸네일 이미지
 * @param {string} props.label - 게시글 제목 또는 라벨
 * @param {string} props.author - 작성자 이름
 * @param {string} props.date - 작성 날짜 (ISO 8601 형식)
 * @param {number} props.viewCount - 조회수
 * @param {'List' | 'Card'} props.layoutType - 레이아웃 타입
 * @param {string} [props.linkTo] - 카드 클릭 시 이동할 링크 (기본값: "/")
 *
 * @returns {JSX.Element} 게시글 정보를 표시하는 카드 UI
 */
const CardBoard: React.FC<CardBoardProps> = ({
    image,
    label,
    author,
    date,
    viewCount,
    layoutType,
    linkTo = '/',
}) => {
    /** 날짜를 Date 객체로 변환 후 포맷팅 */
    const parsedDate = dateParsing(new Date(date));

    return (
        <CardBoardView
            image={image}
            label={label}
            author={author}
            parsedDate={parsedDate}
            viewCount={viewCount}
            layoutType={layoutType}
            linkTo={linkTo}
        />
    );
};

export default CardBoard;
