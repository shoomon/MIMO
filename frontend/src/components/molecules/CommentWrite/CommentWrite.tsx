import { useComment } from '@/hooks/useComment';
import CommentWriteView from './CommentWrite.view';

const CommentWrite = () => {
    const { value, handleChange, handleSubmit } = useComment();

    return (
        <CommentWriteView
            value={value}
            handleChange={handleChange}
            handleSubmit={handleSubmit}
        />
    );
};

export default CommentWrite;
