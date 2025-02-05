import { Link } from 'react-router-dom';

interface ProfileImageViewProps {
    userName: string;
    link: string;
    imgSrc: string;
    size: number;
}

const ProfileImageView = ({
    userName,
    link,
    imgSrc,
    size,
}: ProfileImageViewProps) => {
    return (
        <Link
            to={link}
            className="overflow-hidden rounded-[1.25rem]"
            style={{ width: `${size}px`, height: `${size}px` }}
        >
            <img
                src={imgSrc}
                alt={`${userName} 프로필`}
                className="h-10 w-10 rounded-[1.25rem] object-contain"
                style={{ width: `${size}px`, height: `${size}px` }}
            />
        </Link>
    );
};

export default ProfileImageView;
