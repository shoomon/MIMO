import { createComment } from '@/apis/TeamAPI';
import { useState } from 'react';

interface UseCommentProps {
    teamId: string;
    teamScheduleId: string;
    teamUserId: number;
}

export const useComment = ({
    teamId,
    teamScheduleId,
    teamUserId,
}: UseCommentProps) => {
    // 댓글 내용 관리
    const [value, setValue] = useState<string>('');

    // 인풋 변경
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setValue(e.target.value);
    };

    // 폼 제출
    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        // 댓글 생성 API 호출
        try {
            // 대댓글이 아닌 경우 parentCommentId = null
            // commentId는 서버에서 자동 생성되는 경우, 0이나 null로 넘겨도 상관 없습니다.
            await createComment(
                teamId,
                teamScheduleId,
                teamUserId,
                value, // 실제 댓글 내용
            );

            // 댓글 작성 성공 후 필요한 후속 작업
            // 예: 댓글 목록 다시 불러오기 등
            // 여기서는 일단 작성한 댓글 비우기
            setValue('');
            alert('댓글이 등록되었습니다!');
        } catch (error) {
            console.error(error);
            alert('댓글 등록 중 문제가 발생했습니다.');
        }
    };

    return { value, handleChange, handleSubmit };
};
