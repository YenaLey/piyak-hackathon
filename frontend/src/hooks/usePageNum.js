import { create } from "zustand";

const usePageNum = create((set, get) => ({
  currentPage: 0, // 현재 페이지
  totalPages: 4, // 총 페이지 수

  // 다음 페이지로 이동
  handleNext: () => {
    const { currentPage, totalPages } = get();
    if (currentPage < totalPages - 1) {
      set({ currentPage: currentPage + 1 });
    }
  },

  // 이전 페이지로 이동
  handlePrevious: () => {
    const { currentPage } = get();
    if (currentPage > 0) {
      set({ currentPage: currentPage - 1 });
    }
  },

  // 특정 페이지로 이동
  setPage: (page) => {
    const { totalPages } = get();
    if (page >= 0 && page < totalPages) {
      set({ currentPage: page });
    }
  },
}));

export default usePageNum;
