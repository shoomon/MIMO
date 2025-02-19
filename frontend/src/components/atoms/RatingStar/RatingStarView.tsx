import React from 'react';
import Icon from './../Icon/Icon';

export interface RatingStarViewProps {
    fullStars: number;
    hasHalfStar: boolean;
    emptyStars: number;
    rating: number;
}

/**
 * RatingStarView 컴포넌트
 *
 * 별점 데이터를 기반으로 full star, half star, empty star 아이콘을 렌더링합니다.
 *
 * @param {RatingStarViewProps} props - fullStars, hasHalfStar, emptyStars 정보를 포함한 props
 * @returns {JSX.Element} 렌더링된 별점 아이콘 뷰
 */
const RatingStarView: React.FC<RatingStarViewProps> = ({
    fullStars,
    hasHalfStar,
    emptyStars,
    rating,
}) => {
    return (
        <div className="flex items-center gap-1">
            <div className="flex items-center">
                {Array.from({ length: fullStars }, (_, index) => (
                    <Icon key={`star-full-${index}`} type="svg" id="Star" />
                ))}
                {hasHalfStar && (
                    <Icon key="star-half" type="svg" id="Star-Half" />
                )}
                {Array.from({ length: emptyStars }, (_, index) => (
                    <Icon
                        key={`emptyStar-${index}`}
                        type="svg"
                        id="emptyStar"
                    />
                ))}
            </div>
            <span className="text-md font-extrabold">{rating}</span>
        </div>
    );
};

export default RatingStarView;
