import { Search } from '@/components/atoms';

export interface SearchBarViewProps {
    handleSubmit: (e: React.FormEvent<HTMLFormElement>) => void;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    relatedItem: string[];
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const SearchBarView = ({
    handleSubmit,
    value,
    onChange,
}: SearchBarViewProps) => {
    return (
        <section className="space-y-3 rounded-3xl border border-gray-400 px-6 py-3">
            <Search
                handleSubmit={handleSubmit}
                value={value}
                onChange={onChange}
            />
        </section>
    );
};

export default SearchBarView;
