const dateParsing = (targetDate: Date) => {
    return (
        targetDate.getFullYear() +
        '년 ' +
        (targetDate.getMonth() + 1) +
        '월 ' +
        targetDate.getDate() +
        '일 ' +
        targetDate.getHours() +
        '시 ' +
        targetDate.getMinutes() +
        '분'
    );
};

export default dateParsing;
