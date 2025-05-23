import { useEffect, useState } from 'react';
import { useForm, SubmitHandler } from 'react-hook-form';
import { dateParsing } from '@/utils';
import ProfileImage, {
    ProfileImageProps,
} from './../../atoms/ProfileImage/ProfileImage';
import { useParams } from 'react-router-dom';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';

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
    // 새로 추가: 답글 작성 시 호출될 콜백
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

    const { teamId } = useParams();
    const { data: profileData } = useMyTeamProfile(teamId);
    useEffect(() => {
        if (isEditing) {
            setFocus('commentContent');
        }
    }, [isEditing, setFocus]);

    const onSubmit: SubmitHandler<FormData> = (data) => {
        onUpdate(someCommentId!, data.commentContent);
        setIsEditing(false);
    };

    const handleCancelClick = () => {
        reset({ commentContent: content });
        setIsEditing(false);
    };

    return (
        <div className={`${isReply ? 'pl-8' : ''} flex w-full flex-col gap-2`}>
            <div className="flex h-fit w-full justify-between">
                <div className="flex items-end justify-center gap-3">
                    <div className="flex gap-1">
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
                            {profileData?.nickname == 'name' && (
                                <button
                                    type="button"
                                    onClick={() => setIsEditing(true)}
                                >
                                    수정
                                </button>
                            )}
                            {(profileData?.nickname == 'name' ||
                                profileData?.role == 'LEADER') && (
                                <button
                                    type="button"
                                    onClick={() => onDelete(commentId)}
                                >
                                    삭제
                                </button>
                            )}
                            {onReply && profileData?.role != 'GUEST' && (
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
            <div className="w-full">
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
