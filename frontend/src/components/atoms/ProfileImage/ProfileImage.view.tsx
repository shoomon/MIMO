import { Link } from 'react-router-dom';

interface ProfileImageViewProps {
    userName: string;
    link: string;
    imgSrc: string;
    size: number;
    addStyle?: string;
}

const ProfileImageView = ({
    userName,
    link,
    imgSrc,
    size,
    addStyle,
}: ProfileImageViewProps) => {
    return (
        <Link
            to={link}
            className={`overflow-hidden ${addStyle === undefined ? 'rounded-full' : addStyle}`}
            style={{ width: `${size}px`, height: `${size}px` }}
        >
            <img
                src={imgSrc}
                alt={`${userName} 프로필`}
                className="object-contain"
                style={{ width: `${size}px`, height: `${size}px` }}
            />
        </Link>
    );
};

export default ProfileImageView;
