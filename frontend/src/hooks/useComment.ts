import { createBoardComment, createScheduleComment } from '@/apis/TeamAPI';
import { useState } from 'react';

interface UseCommentProps {
    // 스케줄 댓글인 경우
    teamId?: string;
    teamScheduleId?: string;
    // 게시판 댓글인 경우
    postId?: number;
    // 공통
    teamUserId: number;
    // 대댓글일 경우 (스케줄 댓글용)
    parentCommentId?: number;
    // 대댓글일 경우 (게시판 댓글용)
    parentId?: number;
}

export const useComment = (props: UseCommentProps) => {
    const [value, setValue] = useState<string>('');

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setValue(e.target.value);
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        try {
            // 스케줄 댓글이면 teamScheduleId가 존재하는지 확인
            if (props.teamScheduleId) {
                await createScheduleComment(
                    props.teamId!,
                    props.teamScheduleId,
                    props.teamUserId,
                    value,
                    ...(props.parentCommentId !== undefined
                        ? [props.parentCommentId]
                        : []),
                );
            }
            // 게시판 댓글이면 postId가 존재하는지 확인
            else if (props.postId) {
                await createBoardComment(
                    props.postId.toString(),
                    props.teamUserId,
                    value,
                    ...(props.parentId !== undefined ? [props.parentId] : []),
                );
            } else {
                throw new Error('적절한 댓글 타입이 전달되지 않았습니다.');
            }

            setValue('');
            alert('댓글이 등록되었습니다!');
        } catch (error) {
            console.error('댓글 등록 중 문제 발생:', error);
            alert('댓글 등록 중 문제가 발생했습니다.');
        }
    };

    return { value, handleChange, handleSubmit };
};
