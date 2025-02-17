import React from 'react';

interface CommentWriteViewProps {
    value: string;
    handleSubmit: (e: React.FormEvent<HTMLFormElement>) => void;
    handleChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const CommentWriteView = ({
    value,
    handleSubmit,
    handleChange,
}: CommentWriteViewProps) => {
    return (
        <form
            onSubmit={handleSubmit}
            className="flex w-full items-center gap-2"
        >
            <label htmlFor="comment" className="hidden">
                댓글
            </label>
            <input
                type="text"
                placeholder="댓글을 입력하세요."
                name="comment"
                id="comment"
                value={value}
                onChange={handleChange}
                className="h-fit w-[48.0626rem] rounded-sm border border-gray-300 bg-gray-50 px-4 py-2 leading-normal text-gray-500 placeholder:text-gray-500 focus:outline-none"
            />
            <button
                type="submit"
                className="text-text h-fit rounded-sm border border-gray-600 bg-gray-50 px-2 py-1 text-lg font-bold whitespace-nowrap hover:bg-gray-200"
            >
                댓글 등록
            </button>
        </form>
    );
};

export default CommentWriteView;
