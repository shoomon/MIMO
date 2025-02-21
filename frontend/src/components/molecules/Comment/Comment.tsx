import { useEffect, useState } from 'react';
import { useForm, SubmitHandler } from 'react-hook-form';
import { dateParsing } from '@/utils';
import ProfileImage, {
    ProfileImageProps,
} from './../../atoms/ProfileImage/ProfileImage';
import { useParams } from 'react-router-dom';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';
import { useQueryClient } from '@tanstack/react-query';
import { useModalStore } from '@/stores/modalStore';

interface CommentProps {
    commentId: number;
    someCommentId?: number; // 수정 시 필요한 팀 일정 댓글 ID
    profileImage: ProfileImageProps;
    name: string;
    writedate: string;
    content: string;
    isReply: boolean;
    onDelete: (id: number) => void;
    onUpdate: (someCommentId: number, content: string) => void;
    // 답글 작성 시 호출될 콜백 (선택적)
    onReply?: (parentCommentId: number) => void;
}

interface FormData {
    commentContent: string;
}

const Comment = ({
    commentId,
    someCommentId,
    profileImage,
    writedate,
    content,
    isReply,
    name,
    onDelete,
    onUpdate,
    onReply,
}: CommentProps) => {
    const {
        register,
        handleSubmit,
        reset,
        setFocus,
        formState: { errors },
    } = useForm<FormData>({
        defaultValues: { commentContent: content },
    });

    const [isEditing, setIsEditing] = useState(false);
    const parsedDate = dateParsing(new Date(writedate));
    const { teamId, postId } = useParams<{ teamId: string; postId?: string }>();
    const { data: profileData } = useMyTeamProfile(teamId);
    const queryClient = useQueryClient();
    const { openModal, closeModal } = useModalStore();

    useEffect(() => {
        if (isEditing) {
            setFocus('commentContent');
        }
    }, [isEditing, setFocus]);

    // 삭제 버튼 클릭 시 모달 처리
    const handleDeleteClick = () => {
        openModal({
            title: '댓글을 삭제하시겠습니까?',
            subTitle: '댓글을 삭제하면 답글이 모두 사라져요',
            confirmLabel: '삭제',
            cancelLabel: '취소',
            onDeleteClick: () => {
                onDelete(commentId);
                openModal({
                    title: '댓글이 삭제되었습니다',
                    confirmLabel: '확인',
                    onConfirmClick: () => {
                        if (postId) {
                            queryClient.invalidateQueries({
                                queryKey: ['boardDetail', postId],
                            });
                        }
                        closeModal();
                    },
                });
            },
            onCancelClick: closeModal,
        });
    };

    // 수정 처리 (모달 통해 확인)
    const onSubmit: SubmitHandler<FormData> = (data) => {
        openModal({
            title: '댓글을 수정하시겠습니까?',
            confirmLabel: '수정',
            cancelLabel: '취소',
            onConfirmClick: () => {
                onUpdate(someCommentId!, data.commentContent);
                setIsEditing(false);
                openModal({
                    title: '댓글이 수정되었습니다',
                    confirmLabel: '확인',
                    onConfirmClick: () => {
                        if (postId) {
                            queryClient.invalidateQueries({
                                queryKey: ['boardDetail', postId],
                            });
                        }
                        closeModal();
                    },
                });
            },
            onCancelClick: closeModal,
        });
    };

    const handleCancelClick = () => {
        reset({ commentContent: content });
        setIsEditing(false);
    };

    return (
        <div className={`${isReply ? 'pl-8' : ''} flex w-full flex-col gap-2`}>
            <div className="flex h-fit w-full justify-between">
                <div className="flex items-center justify-center gap-3">
                    <div className="flex items-center gap-1">
                        <ProfileImage
                            userId={profileImage.userId}
                            profileUri={profileImage.profileUri}
                            nickname={name}
                            size={24}
                            addStyle="rounded-lg"
                        />
                        <span className="text-md font-bold">
                            {profileImage.nickname}
                        </span>
                    </div>
                    <span className="text-sm font-normal">{parsedDate}</span>
                </div>
                <div className="text-md text-text flex flex-nowrap gap-2 font-normal">
                    {isEditing ? (
                        <>
                            <button
                                type="button"
                                onClick={handleSubmit(onSubmit)}
                            >
                                저장
                            </button>
                            <button type="button" onClick={handleCancelClick}>
                                취소
                            </button>
                        </>
                    ) : (
                        <>
                            {profileData?.nickname === name && (
                                <button
                                    type="button"
                                    onClick={() => setIsEditing(true)}
                                >
                                    수정
                                </button>
                            )}
                            {(profileData?.nickname === name ||
                                profileData?.role === 'LEADER') && (
                                <button
                                    type="button"
                                    onClick={handleDeleteClick}
                                >
                                    삭제
                                </button>
                            )}
                            {onReply && profileData?.role !== 'GUEST' && (
                                <button
                                    type="button"
                                    onClick={() => onReply(commentId)}
                                >
                                    답글
                                </button>
                            )}
                        </>
                    )}
                </div>
            </div>
            <div className="mb-3 w-full">
                {isEditing ? (
                    <form onSubmit={handleSubmit(onSubmit)}>
                        <textarea
                            className="text-md w-full resize-none rounded-sm border border-gray-300 px-4 py-3 font-medium focus:outline-none"
                            rows={3}
                            {...register('commentContent', {
                                required: '댓글 내용을 입력해주세요.',
                                maxLength: {
                                    value: 500,
                                    message:
                                        '댓글은 최대 500자까지 입력할 수 있습니다.',
                                },
                            })}
                        />
                        {errors.commentContent && (
                            <p className="text-fail mt-1 text-sm">
                                {errors.commentContent.message}
                            </p>
                        )}
                    </form>
                ) : (
                    <p className="text-md w-full font-medium whitespace-pre-wrap">
                        {content}
                    </p>
                )}
            </div>
        </div>
    );
};

export default Comment;
