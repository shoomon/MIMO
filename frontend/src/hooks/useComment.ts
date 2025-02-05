import { useState } from "react";

export const useComment = () => {

    // api에 필요한 변수 받는거 추가해야함
    
    const [value, setValue] = useState<string>(""); 
    const handleChange:(e: React.ChangeEvent<HTMLInputElement>) => void = (e) => {
      setValue(e.currentTarget.value);
    }

    const handleSubmit:(e: React.FormEvent<HTMLFormElement>) => void = (e) => {
      e.preventDefault();
      
      // 댓글 다는 api 필요
      alert("제출!");
    }
    
    return {value, handleChange, handleSubmit}
}

