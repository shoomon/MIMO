import { ROUTER_CONFIG } from "@/constants/navItems";
import { useLocation } from "react-router-dom";

export const useNavActive = (link:string) => {

    const location = useLocation();

    return location.pathname.includes(link);
}

export const useBreadcrumbs = () => {

    const location = useLocation();
    const segments = location.pathname.split("/").filter(Boolean);

    const filteredSegments = segments.filter(segment => isNaN(Number(segment)));
    
    const breadcrumbs = filteredSegments.map(segment => ({
        to: `/${segment}`,
        label: ROUTER_CONFIG[segment]?.name || segment
    }));

    return breadcrumbs;
}


