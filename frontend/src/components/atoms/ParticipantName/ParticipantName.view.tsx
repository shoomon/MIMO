export interface ParticipantNameProps {
    name: string;
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const ParticipantNameView = ({ name, onClick }: ParticipantNameProps) => {
    return (
        <button
            onClick={onClick}
            className="bg-dark cursor-pointer rounded-full px-4 py-1 font-bold text-white"
        >
            {name}
        </button>
    );
};

export default ParticipantNameView;
