import React from "react";
import "./PageNavigation.css";

export default function PageNavigation({
  currentPage,
  totalPages,
  onNext,
  onPrevious,
  data,
}) {
  return (
    <div className="navigation-container">
      <button
        onClick={onPrevious}
        disabled={currentPage === 0}
        className={`navigation-button ${
          currentPage === 0 ? "button-disabled" : "button-active"
        }`}
      >
        이전
      </button>
      <button
        onClick={onNext}
        disabled={currentPage === totalPages - 1}
        className={`navigation-button ${
          currentPage === totalPages - 1 ? "button-disabled" : "button-active"
        }`}
      >
        다음
      </button>
    </div>
  );
}
