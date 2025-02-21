import ProfileImageView from './ProfileImage.view';

export interface ProfileImageProps {
    userId?: string;
    nickname: string;
    profileUri?: string;
    size?: number;
    addStyle?: string;
}

const ProfileImage = ({
    userId,
    profileUri,
    nickname,
    size = 40,
    addStyle,
}: ProfileImageProps) => {
    const user_url = userId ? `/user/${userId}` : null;
    const img_url = profileUri === undefined ? '/ProfileImage.png' : profileUri;

    return (
        <ProfileImageView
            link={user_url}
            nickname={nickname}
            profileUri={img_url}
            size={size}
            addStyle={addStyle}
        />
    );
};

export default ProfileImage;
