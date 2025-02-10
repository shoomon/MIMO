import { Link } from 'react-router-dom';

interface ProfileImageViewProps {
    userName?: string;
    link: string;
    profileUri: string;
    size: number;
    addStyle?: string;
}

const ProfileImageView = ({
    userName,
    link,
    profileUri,
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
                src={profileUri}
                alt={`${userName} 프로필`}
                className="object-contain"
                style={{ width: `${size}px`, height: `${size}px` }}
            />
        </Link>
    );
};

export default ProfileImageView;
