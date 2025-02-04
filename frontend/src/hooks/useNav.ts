import { useLocation } from "react-router-dom";

export const useNavActive = (link:string) => {

    const location = useLocation();

    return location.pathname.includes(link);
}