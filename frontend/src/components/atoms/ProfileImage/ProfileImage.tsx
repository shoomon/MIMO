import ProfileImageView from './ProfileImage.view';

export interface ProfileImageProps {
    userId: string;
    userName?: string;
    profileUri?: string;
    size?: number;
    addStyle?: string;
}

const ProfileImage = ({
    userId,
    profileUri,
    userName,
    size = 40,
    addStyle,
}: ProfileImageProps) => {
    const user_url = `/${userId}`;
    const img_url = profileUri === undefined ? '/ProfileImage.png' : profileUri;

    return (
        <ProfileImageView
            link={user_url}
            userName={userName}
            profileUri={img_url}
            size={size}
            addStyle={addStyle}
        />
    );
};

export default ProfileImage;
