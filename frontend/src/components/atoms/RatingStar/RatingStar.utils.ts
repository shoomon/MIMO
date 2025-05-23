/**
 * 주어진 rating 값을 기반으로 별점 데이터를 계산합니다.
 * 예를 들어, rating이 4.3이면 fullStars는 4이고, 소수점이 0.5 미만이므로 half star는 표시하지 않습니다.
 * 반면, rating이 4.7이면 fullStars는 4이고, 소수점이 0.5 이상이면 half star를 표시합니다.
 * 전체 별 개수는 5개가 되도록 빈 별(empty star) 개수를 계산합니다.
 *
 * @param {number} rating - 입력된 별점 값 (실수)
 * @returns {{
 *   fullStars: number,
 *   hasHalfStar: boolean,
 *   emptyStars: number
 * }}
 *  fullStars: 전체 별 아이콘으로 표시할 개수,
 *  hasHalfStar: 반 별 아이콘을 표시할지 여부,
 *  emptyStars: 빈 별 아이콘으로 표시할 개수
 */
export const getStarRatingData = (
    rating: number,
): { fullStars: number; hasHalfStar: boolean; emptyStars: number } => {
    const clampedRating = Math.min(5, Math.max(0, rating));
    const fullStars = Math.floor(clampedRating);
    const hasHalfStar = clampedRating - fullStars >= 0.5;
    const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);
    return { fullStars, hasHalfStar, emptyStars };
};
