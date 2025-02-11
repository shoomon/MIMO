import { useEffect, useState } from "react";

export const usePagenationList = (currentPage: number, pageSize: number) => {

    const [currentPageList, setCurrentPageList] = useState<number[]>([1, 2, 3, 4]);
  
    useEffect(() => {
      const pageLineSize = 4;
      const pagelist: number[] = [];
      const currentLastPage =
          currentPage + pageLineSize > pageSize ? pageSize : currentPage + pageLineSize;
  
      for (let i = currentPage; i <= currentLastPage; i++) {
          pagelist.push(i);
      }
  
      setCurrentPageList(pagelist);
    }, [currentPage, pageSize]);

    console.log(currentPageList);

    return { currentPageList };
} 