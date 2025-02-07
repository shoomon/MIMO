import ProfileImageView from './ProfileImage.view';

export interface ProfileImageProps {
    userId: string;
    userName: string;
    imgSrc?: string;
    size?: number;
    addStyle?: string;
}

const ProfileImage = ({
    userId,
    imgSrc,
    userName,
    size = 40,
    addStyle,
}: ProfileImageProps) => {
    const user_url = `/${userId}`;
    const img_url = imgSrc === undefined ? '/ProfileImage.png' : imgSrc;

    return (
        <ProfileImageView
            link={user_url}
            userName={userName}
            imgSrc={img_url}
            size={size}
            addStyle={addStyle}
        />
    );
};

export default ProfileImage;
