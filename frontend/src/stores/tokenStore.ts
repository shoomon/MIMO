import { create } from "zustand";
import { persist } from "zustand/middleware";

export interface TokenStore {
  accessToken: string | null;
  setAccessToken: (token:string) => void;
  logout: () => void;
}

export const useTokenStore = create<TokenStore>()(

  persist(
    (set) => ({
      accessToken: null,
      setAccessToken: (token) => set({accessToken: token}),
      logout: () => set({accessToken: null})
    }),{
      name: "auth-storage"
    }
  )
);