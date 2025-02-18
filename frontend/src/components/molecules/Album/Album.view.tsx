import { Title } from '@/components/atoms';
import { Link } from 'react-router-dom';

export interface AlbumItemProps {
    boardId: string;
    imageUri: string;
}

export interface AlbumProps {
    id: string;
    images: AlbumItemProps[];
}

const AlbumView = ({ images = [], id }: AlbumProps) => {
    if (!Array.isArray(images) || images.length === 0) {
        return <p>앨범에 아무것도 없어요</p>;
    }

    return (
        <div className="flex w-[28rem] flex-col gap-4">
            <Title label="앨범" to={`album`} />
            <ul className="grid w-full grid-cols-3 gap-2">
                {images.map((item) => (
                    <li key={item.boardId}>
                        <Link to={`${item.boardId}`} className="inline-block">
                            <img
                                src={item.imageUri}
                                alt=""
                                className="h-36 w-36 rounded-lg object-cover"
                            />
                        </Link>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default AlbumView;
