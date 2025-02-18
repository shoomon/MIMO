import FilterDropDownView from './FilterDropDown.view';

export interface FilterDropDownProps {
    active: boolean;
    currentCondition: string;
    conditionList: string[];
    onClickList: (e: React.MouseEvent<HTMLButtonElement>) => void;
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const FilterDropDown = ({
    active,
    currentCondition,
    conditionList,
    onClick,
    onClickList,
}: FilterDropDownProps) => {
    const filteredConditionList = conditionList.filter(
        (item) => item !== currentCondition,
    );

    return (
        <FilterDropDownView
            active={active}
            currentCondition={currentCondition}
            conditionList={filteredConditionList}
            onClick={onClick}
            onClickList={onClickList}
        />
    );
};

export default FilterDropDown;
