import { useComment } from '@/hooks/useComment';
import CommentWriteView from './CommentWrite.view';

interface CommentWriteProps {
    teamId: string; // 팀 ID (반드시 유효한 문자열)
    teamUserId: number; // 팀 사용자 ID
    postId?: number; // 게시판 댓글인 경우 유효한 postId (예: 123)
    // 게시판 대댓글의 경우 추가적으로 parentId를 전달할 수 있습니다.
    parentId?: number;
    // 스케줄 댓글인 경우 아래 props를 사용합니다.
    teamScheduleId?: string;
    parentCommentId?: number;
    onCommentCreated?: () => void;
}

const CommentWrite = ({
    teamId,
    teamUserId,
    postId,
    parentId,
    teamScheduleId,
    parentCommentId,
    onCommentCreated,
}: CommentWriteProps) => {
    // 게시판 댓글인 경우 postId가 전달되어야 하며,
    // 스케줄 댓글인 경우 teamScheduleId가 전달되어야 합니다.
    const { value, handleChange, handleSubmit } = useComment(
        {
            teamId,
            teamUserId,
            postId, // 게시판 댓글용
            teamScheduleId, // 스케줄 댓글용
            parentId, // 게시판 대댓글용
            parentCommentId, // 스케줄 대댓글용
        },
        onCommentCreated,
    );

    return (
        <CommentWriteView
            value={value}
            handleChange={handleChange}
            handleSubmit={handleSubmit}
        />
    );
};

export default CommentWrite;
