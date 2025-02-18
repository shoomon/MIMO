import { getAlbumImageList } from '@/apis/TeamBoardAPI';
import { useQuery } from '@tanstack/react-query';
import { useParams, useNavigate } from 'react-router-dom';

const TeamAlbum = () => {
    const { teamId } = useParams() as { teamId: string };
    const navigate = useNavigate();

    const { data: albumData } = useQuery({
        queryKey: ['albumdata', teamId],
        queryFn: () => getAlbumImageList(teamId),
    });

    const handleClick = (boardId: string, teamBoardId: string) => {
        navigate(`/team/${teamId}/board/${teamBoardId}/post/${boardId}`);
    };

    return (
        <div className="grid grid-cols-1 gap-4 p-4 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
            {albumData?.images?.map((image) => {
                if (!image.imageUri) return null;
                return (
                    <div
                        key={image.boardId}
                        className="group relative cursor-pointer overflow-hidden rounded-lg transition-all duration-300 hover:shadow-xl"
                        onClick={() =>
                            handleClick(image.boardId, image.teamBoardId)
                        }
                    >
                        <img
                            src={image.imageUri}
                            alt={`게시물 ${image.boardId}`}
                            className="block h-full w-full transform object-cover transition-transform duration-300 group-hover:-translate-y-1 group-hover:scale-105 group-hover:rotate-3"
                        />
                        <div className="absolute inset-0 bg-black opacity-0 transition-opacity duration-300 group-hover:opacity-20" />
                    </div>
                );
            })}
        </div>
    );
};

export default TeamAlbum;
