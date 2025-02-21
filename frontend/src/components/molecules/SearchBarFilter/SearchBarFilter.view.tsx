import { FilterDropDown } from '@/components/atoms';
import { FilterDropDownProps } from '@/components/atoms/FilterDropDown/FilterDropDown';

export interface SearchBarFilterProps {
    filterProps: FilterDropDownProps;
    handleSearchSubmit: (e: React.FormEvent<HTMLFormElement>) => void;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const SearchBarFilterView = ({
    filterProps,
    handleSearchSubmit,
    value,
    onChange,
}: SearchBarFilterProps) => {
    return (
        <section className="flex gap-1">
            <FilterDropDown {...filterProps} />
            <form
                action=""
                onSubmit={handleSearchSubmit}
                className="flex gap-1"
            >
                <section>
                    <label htmlFor="filterSearch"></label>
                    <input
                        type="text"
                        name="filterSearch"
                        id="filterSearch"
                        value={value}
                        onChange={onChange}
                        className="w-[37.5rem] border border-gray-300 bg-gray-50 px-2 py-2"
                    />
                </section>
                <button
                    type="submit"
                    className="bg-brand-primary-400 h-fit px-4 py-2 font-semibold text-white"
                >
                    검색
                </button>
            </form>
        </section>
    );
};

export default SearchBarFilterView;
