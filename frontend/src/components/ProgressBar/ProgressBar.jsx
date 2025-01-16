import "./ProgressBar.css";

export default function ProgressBar({ currentPage, totalPages }) {
  const progressPercentage = ((currentPage + 1) / totalPages) * 100;

  return (
    <div className="progress-bar-container">
      <div
        className="progress-bar"
        style={{
          width: `${progressPercentage}%`,
        }}
      ></div>
    </div>
  );
}
