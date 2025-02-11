import React from 'react';
import { useComment } from '@/hooks/useComment';
import CommentWriteView from './CommentWrite.view';

interface CommentWriteProps {
    userId?: number;
    teamId?: number;
    teamScheduleId?: number;
    teamUserId?: number;
}

const CommentWrite = ({
    userId,
    teamId,
    teamScheduleId,
    teamUserId,
}: CommentWriteProps) => {
    // 필요한 값들을 useComment 훅에 props로 전달
    const { value, handleChange, handleSubmit } = useComment({
        userId,
        teamId,
        teamScheduleId,
        teamUserId,
    });

    return (
        <CommentWriteView
            value={value}
            handleChange={handleChange}
            handleSubmit={handleSubmit}
        />
    );
};

export default CommentWrite;
