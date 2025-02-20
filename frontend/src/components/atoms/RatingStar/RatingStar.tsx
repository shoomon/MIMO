import RatingStarView from './RatingStarView';
import { getStarRatingData } from './RatingStar.utils';

interface RatingStarProps {
    reviewScore: number;
    reviewCount?: number;
}

/**
 * 별점 컴포넌트
 *
 * 주어진 평점을 기반으로 별점 데이터를 계산한 후, UI를 렌더링합니다.
 */
const RatingStar = ({ reviewScore, reviewCount }: RatingStarProps) => {
    const { fullStars, hasHalfStar, emptyStars } =
        getStarRatingData(reviewScore);

    return (
        <RatingStarView
            reviewCount={reviewCount}
            reviewScore={reviewScore}
            fullStars={fullStars}
            hasHalfStar={hasHalfStar}
            emptyStars={emptyStars}
        />
    );
};

export default RatingStar;
