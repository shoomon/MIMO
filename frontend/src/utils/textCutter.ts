/**
 * 문자열을 지정된 길이로 자르고 '...'을 추가하는 유틸리티 함수
 *
 * @param {string} content - 자를 대상 문자열
 * @param {number} lengthLimit - 최대 문자열 길이
 * @returns {string} 지정된 길이를 초과하면 '...'을 추가한 문자열
 *
 * @example
 * ```ts
 * textCutter("안녕하세요, 반갑습니다!", 10);
 * // 출력: "안녕하세요, 반..."
 * ```
 */
const textCutter = (content: string, lengthLimit: number) => {
    if (content.length > lengthLimit) {
        return content.substring(0, lengthLimit) + '···';
    }
    return content;
};
export default textCutter;
