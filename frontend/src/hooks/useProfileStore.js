import { create } from "zustand";

const useProfileStore = create((set) => ({
  profiles: [], // 여러 프로필을 저장하는 배열
  addProfile: (profile) =>
    set((state) => ({
      profiles: [...state.profiles, profile], // 새로운 프로필 추가
    })),
  updateProfile: (index, updatedProfile) =>
    set((state) => {
      const updatedProfiles = [...state.profiles];
      updatedProfiles[index] = {
        ...updatedProfiles[index],
        ...updatedProfile,
      };
      return { profiles: updatedProfiles };
    }),
  deleteProfile: (index) =>
    set((state) => ({
      profiles: state.profiles.filter((_, i) => i !== index), // 특정 프로필 삭제
    })),
}));

export default useProfileStore;
