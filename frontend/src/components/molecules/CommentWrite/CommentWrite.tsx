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
