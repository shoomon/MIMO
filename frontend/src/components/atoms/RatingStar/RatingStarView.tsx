import React from 'react';
import Icon from './../Icon/Icon';

export interface RatingStarViewProps {
    fullStars: number;
    hasHalfStar: boolean;
}

/**
 * RatingStarView 컴포넌트
 *
 * 별점 데이터를 기반으로 full star와 half star 아이콘을 렌더링합니다.
 *
 * @param {RatingStarViewProps} props - fullStars와 hasHalfStar 정보를 포함한 props
 * @returns {JSX.Element} 렌더링된 별점 아이콘 뷰
 */
const RatingStarView: React.FC<RatingStarViewProps> = ({
    fullStars,
    hasHalfStar,
}) => {
    return (
        <div className="flex items-center">
            {Array.from({ length: fullStars }, (_, index) => (
                <Icon key={`star-full-${index}`} type="svg" id="Star" />
            ))}
            {hasHalfStar && <Icon key="star-half" type="svg" id="Star-Half" />}
        </div>
    );
};

export default RatingStarView;
