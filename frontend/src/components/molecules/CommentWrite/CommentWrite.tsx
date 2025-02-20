import { useComment } from '@/hooks/useComment';
import CommentWriteView from './CommentWrite.view';
import { useModalStore } from '@/stores/modalStore';
import { useQueryClient } from '@tanstack/react-query';

interface CommentWriteProps {
    teamId: string; // 유효한 팀 ID
    teamUserId: number; // 팀 사용자 ID
    postId?: string; // 게시판 댓글용 postId (예: "123")
    parentId?: number; // 게시판 대댓글의 경우 부모 댓글 ID
    teamScheduleId?: string;
    parentCommentId?: number;
    // 스케줄 댓글 관련 props는 필요 시 추가
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
    const { value, handleChange, submitComment } = useComment({
        teamId,
        teamUserId,
        postId,
        parentId,
        teamScheduleId,
        parentCommentId,
    });

    const { openModal, closeModal } = useModalStore();
    const queryClient = useQueryClient();

    const handleSubmitWithModal = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        // 확인 모달을 띄워 사용자가 등록 여부를 확인
        openModal({
            title: '댓글을 등록하시겠습니까?',
            confirmLabel: '등록',
            cancelLabel: '취소',
            onConfirmClick: async () => {
                await submitComment();

                if (postId) {
                    queryClient.invalidateQueries({
                        queryKey: ['boardDetail', postId],
                    });
                } else if (teamScheduleId) {
                    queryClient.invalidateQueries({
                        queryKey: ['scheduleDetail', teamScheduleId],
                    });
                    if (onCommentCreated) {
                        onCommentCreated();
                    }
                }

                closeModal();
            },
            onCancelClick: closeModal,
        });
    };

    return (
        <CommentWriteView
            value={value}
            handleChange={handleChange}
            handleSubmit={handleSubmitWithModal}
        />
    );
};

export default CommentWrite;
