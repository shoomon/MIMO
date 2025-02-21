/**
 * 객체 내부에서 undefined 값을 가진 프로퍼티의 경로를 재귀적으로 탐색합니다.
 * null 값은 허용하므로 undefined가 아닌 경우 재귀 탐색을 계속합니다.
 *
 * @param obj - 탐색할 대상 (object 혹은 array)
 * @param parentPath - 현재까지의 경로 (재귀용)
 * @returns undefined인 프로퍼티의 경로 목록
 */
function getUndefinedPaths(obj: unknown, parentPath: string = ''): string[] {
    // obj가 객체 또는 배열인 경우에만 탐색
    if (obj !== null && typeof obj === 'object') {
        let paths: string[] = [];
        // 배열과 객체 모두 순회
        for (const key in obj) {
            if (!Object.prototype.hasOwnProperty.call(obj, key)) continue;
            const newPath = parentPath ? `${parentPath}.${key}` : key;
            const value = (obj as Record<string, unknown>)[key];
            // undefined면 경로 추가
            if (value === undefined) {
                paths.push(newPath);
            } else {
                // null은 허용하므로, 재귀 호출 시 undefined 체크만 함
                paths = paths.concat(getUndefinedPaths(value, newPath));
            }
        }
        return paths;
    }
    return [];
}

export function typeChecker<T extends object>(
    data: T | undefined | null,
): { isValid: boolean; undefinedFields: string[] } {
    // 전체 데이터가 undefined인 경우 에러 반환
    if (data === undefined) {
        return {
            isValid: false,
            undefinedFields: ['전체 데이터가 undefined입니다.'],
        };
    }

    // 전체 데이터가 null이면 정상 처리 (내부 undefined 체크 불필요)
    if (data === null) {
        return {
            isValid: true,
            undefinedFields: [],
        };
    }

    // 재귀적으로 내부 undefined 값 탐색
    const undefinedFields = getUndefinedPaths(data);

    return {
        isValid: undefinedFields.length === 0,
        undefinedFields,
    };
}
