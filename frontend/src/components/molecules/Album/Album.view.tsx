import { Title } from '@/components/atoms';
import { Link } from 'react-router-dom';

export interface AlbumItemProps {
    imgId: string;
    imgSrc: string;
}

export interface AlbumProps {
    id: string;
    items: AlbumItemProps[];
}

const AlbumView = ({ items = [], id }: AlbumProps) => {
    console.log('AlbumView items:', items);

    if (!Array.isArray(items) || items.length === 0) {
        return <p>앨범에 아무것도 없어요</p>;
    }

    return (
        <div className="flex w-[28rem] flex-col gap-4">
            <Title label="앨범" to={`/album/${id}`} />
            <ul className="grid w-full grid-cols-3 gap-2">
                {items.images.map((item) => (
                    <li key={item.imgId}>
                        <Link to={item.imgId} className="inline-block">
                            <img
                                src={item.imgSrc}
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
