import { useState } from "react";
import Form1Picker from "@/components/Form1Picker/Form1Picker";
import Form2Picker from "@/components/Form2Picker/Form2Picker";
import Form3Picker from "@/components/Form3Picker/Form3Picker";
import Form4Picker from "@/components/Form4Picker/Form4Picker";
import ProgressBar from "@/components/ProgressBar/ProgressBar";
import PageNavigation from "@/components/PageNavigation/PageNavigation";
import "@/styles/SendMail.css";
import MarkIcon from "@/image/mark.svg";

export default function SendMail() {
  const totalPages = 4;
  const [currentPage, setCurrentPage] = useState(0);

  const handleNext = () => {
    if (currentPage < totalPages - 1) {
      setCurrentPage((prev) => prev + 1); // 다음 페이지로 이동
    }
  };

  const handlePrevious = () => {
    if (currentPage > 0) {
      setCurrentPage((prev) => prev - 1); // 이전 페이지로 이동
    }
  };

  const renderTitle = () => {
    switch (currentPage) {
      case 0:
        return <h2><img src={MarkIcon} alt="Mark" />내 정보 선택하기</h2>;
      case 1:
        return <h2><img src={MarkIcon} alt="Mark" />메일 보낼 사람 선택하기</h2>;
      case 2:
        return <h2><img src={MarkIcon} alt="Mark" />내용 선택하기</h2>;
      case 3:
        return <h2><img src={MarkIcon} alt="Mark" />Ai가 맞는 이메일을 만들어주었어요!</h2>;
      default:
        return null;
    }
  };

  const renderPage = () => {
    switch (currentPage) {
      case 0:
        return <Form1Picker />;
      case 1:
        return <Form2Picker />;
      case 2:
        return <Form3Picker />;
      case 3:
        return <Form4Picker />;
      default:
        return null;
    }
  };

  return (
    <div className="send-mail-container">
      <div className="send-mail-header">
        <span>메일 보내기</span>
      </div>
      <div className="send-mail-progress-bar-container">
        {renderTitle()}
        <ProgressBar currentPage={currentPage} totalPages={totalPages} />
      </div>
      <div className="send-mail-content-box">{renderPage()}</div>
      <PageNavigation
        currentPage={currentPage}
        totalPages={totalPages}
        onNext={handleNext}
        onPrevious={handlePrevious}
      />
    </div>
  );
}
