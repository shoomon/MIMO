import { useComment } from '@/hooks/useComment';
import CommentWriteView from './CommentWrite.view';

interface CommentWriteProps {
    teamId?: string;
    teamScheduleId?: string;
    teamUserId?: number;
}

const CommentWrite = ({
    teamId = '',
    teamScheduleId = '',
    teamUserId = 0,
}: CommentWriteProps) => {
    // 필요한 값들을 useComment 훅에 props로 전달
    const { value, handleChange, handleSubmit } = useComment({
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
