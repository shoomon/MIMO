import { Icon } from '@/components/atoms';

interface FilterDropDwonView {
    active: boolean;
    currentCondition: string;
    conditionList: string[];
    onClickList: (e: React.MouseEvent<HTMLButtonElement>) => void;
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const FilterDropDownView = ({
    active,
    currentCondition,
    conditionList,
    onClickList,
    onClick,
}: FilterDropDwonView) => {
    return (
        <div className="flex flex-col bg-gray-50">
            <button
                className="flex cursor-pointer gap-1 border border-gray-300 px-3 py-2"
                onClick={onClick}
                data-value={currentCondition}
            >
                <span className="block w-12 text-left font-semibold text-gray-900">
                    {currentCondition}
                </span>
                <div
                    className={`flex transform items-center justify-center transition-transform duration-300 ease-in-out ${active ? 'rotate-180' : null}`}
                >
                    <Icon type="svg" id="DropDown" />
                </div>
            </button>
            <div
                className={`flex flex-col overflow-hidden border border-gray-300 transition-all duration-300 ease-in-out ${
                    active
                        ? 'max-h-[200px] opacity-100'
                        : 'max-h-0 border-0 opacity-0'
                }`}
            >
                {conditionList.map((item) => (
                    <button
                        key={item}
                        className="flex gap-1 px-3 py-2"
                        onClick={onClickList}
                        data-value={item}
                    >
                        <span className="block w-12 text-left font-semibold text-gray-900">
                            {item}
                        </span>
                    </button>
                ))}
            </div>
        </div>
    );
};

export default FilterDropDownView;
