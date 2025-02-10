const dateParsing = (targetDate: Date, onlyDate: boolean = false) => {
    const dateStr =
        targetDate.getFullYear() +
        '년 ' +
        (targetDate.getMonth() + 1) +
        '월 ' +
        targetDate.getDate() +
        '일';

    if (onlyDate) {
        return dateStr;
    }

    return (
        dateStr +
        ' ' +
        targetDate.getHours() +
        '시 ' +
        targetDate.getMinutes() +
        '분'
    );
};

export default dateParsing;
