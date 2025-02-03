import ProfileImageView from './ProfileImage.view';

interface ProfileImageProps {
    userId: string;
    userName: string;
    imgSrc?: string;
}

const ProfileImage = ({ userId, imgSrc, userName }: ProfileImageProps) => {
    const user_url = `/${userId}`;
    const img_url = imgSrc === undefined ? '/ProfileImage.png' : imgSrc;

    return (
        <ProfileImageView
            link={user_url}
            userName={userName}
            imgSrc={img_url}
        />
    );
};

export default ProfileImage;
