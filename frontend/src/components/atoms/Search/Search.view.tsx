import { Icon } from '@/components/atoms';

export interface SearchProps {
    handleSubmit: (e: React.FormEvent<HTMLFormElement>) => void;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const SearchView = ({ handleSubmit, value, onChange }: SearchProps) => {
    return (
        <form onSubmit={handleSubmit} method="GET" className="flex gap-2">
            <label htmlFor="search">
                <Icon type="svg" id="Search" />
            </label>
            <input
                type="text"
                name="search"
                id="search"
                value={value}
                onChange={onChange}
                className="w-full focus:outline-0"
            />
        </form>
    );
};

export default SearchView;
