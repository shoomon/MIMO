import { Link } from 'react-router-dom';

interface ProfileImageViewProps {
    userName: string;
    link: string;
    imgSrc: string;
}

const ProfileImageView = ({
    userName,
    link,
    imgSrc,
}: ProfileImageViewProps) => {
    return (
        <Link to={link} className="h-10 w-10 overflow-hidden rounded-[1.25rem]">
            <img
                src={imgSrc}
                alt={`${userName} 프로필`}
                className="h-10 w-10 rounded-[1.25rem] object-contain"
            />
        </Link>
    );
};

export default ProfileImageView;
