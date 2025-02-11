import { useEffect, useState } from 'react';
import { useForm, SubmitHandler } from 'react-hook-form';
import { dateParsing } from '@/utils';
import ProfileImage, {
    ProfileImageProps,
} from './../../atoms/ProfileImage/ProfileImage';

interface CommentProps {
    commentId: number;
    profileImage: ProfileImageProps;
    name: string;
    writedate: string;
    content: string;
    isReply: boolean;
    onDelete: (id: number) => void;
    onUpdate: (id: number, newContent: string) => void;
}

interface FormData {
    commentContent: string;
}

const Comment = ({
    commentId,
    profileImage,
    writedate,
    content,
    isReply,
    name,
    onDelete,
    onUpdate,
}: CommentProps) => {
    const {
        register,
        handleSubmit,
        reset,
        setFocus,
        formState: { errors },
    } = useForm<FormData>({
        defaultValues: {
            commentContent: content,
        },
    });

    const [isEditing, setIsEditing] = useState(false);
    const parsedDate = dateParsing(new Date(writedate));

    // 편집 모드 전환 시 textarea에 포커스 설정
    useEffect(() => {
        if (isEditing) {
            setFocus('commentContent');
        }
    }, [isEditing, setFocus]);

    const onSubmit: SubmitHandler<FormData> = (data: FormData) => {
        onUpdate(commentId, data.commentContent);
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
                            <button
                                type="button"
                                onClick={() => setIsEditing(true)}
                            >
                                수정
                            </button>
                            <button
                                type="button"
                                onClick={() => onDelete(commentId)}
                            >
                                삭제
                            </button>
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
