import RatingStarView from './RatingStarView';
import { getStarRatingData } from './RatingStar.utils';

export interface RatingStarProps {
    rating: number;
}

/**
 * RatingStar 컨테이너 컴포넌트
 *
 * 주어진 rating 값을 기반으로 별점 데이터를 계산한 후,
 * 해당 데이터를 RatingStarView 컴포넌트에 전달하여 별점 아이콘을 렌더링합니다.
 *
 * @param {RatingStarProps} props - rating 값을 포함한 props
 * @returns {JSX.Element} 렌더링된 별점 컴포넌트
 */
const RatingStar: React.FC<RatingStarProps> = ({ rating }) => {
    const { fullStars, hasHalfStar, emptyStars } = getStarRatingData(rating);

    return (
        <RatingStarView
            rating={rating}
            fullStars={fullStars}
            hasHalfStar={hasHalfStar}
            emptyStars={emptyStars}
        />
    );
};

export default RatingStar;
