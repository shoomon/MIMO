import { Link } from 'react-router-dom';

interface ProfileImageViewProps {
    nickname: string;
    link: string | null;
    profileUri: string;
    size: number;
    addStyle?: string;
}

const ProfileImageView = ({
    nickname,
    link,
    profileUri,
    size,
    addStyle,
}: ProfileImageViewProps) => {
    const Component = link ? Link : 'div';

    return (
        <Component
            to={link ? link : ''}
            className={`overflow-hidden ${addStyle === undefined ? 'rounded-full' : addStyle}`}
            style={{ width: `${size}px`, height: `${size}px` }}
        >
            <img
                src={profileUri}
                alt={`${nickname} 프로필`}
                className="object-contain"
                style={{ width: `${size}px`, height: `${size}px` }}
            />
        </Component>
    );
};

export default ProfileImageView;
