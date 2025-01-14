import { create } from "zustand";
import { persist } from "zustand/middleware";

const useUserInfo = create(
  persist(
    (set) => ({
      name: "",
      email: "",
      setUserInfo: (name, email) => set({ name, email }),
      clearUserInfo: () => set({ name: "", email: "" }),
    }),
    {
      name: "user-info", // localStorage에 저장될 키
    }
  )
);

export default useUserInfo;
