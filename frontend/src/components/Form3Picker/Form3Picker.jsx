import React, { useState, useEffect, useCallback } from "react";
import "./Form3Picker.css";
import { sendForm } from "@/services/axios";

export default function Form3Picker({ handleNext }) {
  const initialFormData = (() => {
    const savedData = JSON.parse(localStorage.getItem("save-form3"));
    if (savedData) {
      return {
        situation: savedData.situation || "",
        desiredAnswer: savedData.desiredAnswer || "",
        tone: savedData.tone || "",
      };
    }
    return {
      situation: "",
      desiredAnswer: "",
      tone: "",
    };
  })();

  const [formData, setFormData] = useState(initialFormData);
  const [selectedLanguage, setSelectedLanguage] = useState("korean");
  const [isLoading, setIsLoading] = useState(false); // 로딩 상태 추가

  const saveDataToLocalStorage = useCallback(() => {
    localStorage.setItem("save-form3", JSON.stringify(formData));
    console.log("save-form3에 데이터가 저장되었습니다.", formData);
  }, [formData]);

  useEffect(() => {
    return () => {
      saveDataToLocalStorage();
    };
  }, [saveDataToLocalStorage]);

  const handleToneChange = (tone) => {
    setFormData((prev) => ({ ...prev, tone }));
  };

  const handleLanguageChange = (language) => {
    setSelectedLanguage(language);
  };

  const handleSubmit = async () => {
    try {
      setIsLoading(true); // 로딩 시작
      // 로컬스토리지에서 데이터 가져오기
      const form1 = JSON.parse(localStorage.getItem("save-form1")) || {};
      const form2 = JSON.parse(localStorage.getItem("save-form2")) || {};
      const form3 = JSON.parse(localStorage.getItem("save-form3")) || {};

      // 최종 JSON 구조 생성
      const finalForm = {
        form1,
        form2,
        form3,
        language: selectedLanguage, // 기본 언어를 "korean"으로 설정
        state: "", // 초기 상태 설정
      };

      console.log("최종 제출 데이터:", finalForm);

      // Axios 요청 보내기
      const response = await sendForm(finalForm);
      if (response.status === 200) {
        console.log("성공적으로 제출되었습니다:", response.data);

        // response.data를 로컬스토리지에 save-form4로 저장
        localStorage.setItem("save-form4", JSON.stringify(response.data));
        alert("메일이 생성되었습니다!");
      } else {
        console.error("제출 중 오류 발생:", response);
        alert("제출 중 문제가 발생했습니다.");
      }
    } catch (error) {
      console.error("제출 처리 중 오류 발생:", error);
      alert("제출 중 오류가 발생했습니다.");
    } finally {
      setIsLoading(false); // 로딩 종료
      handleNext();
    }
  };

  return (
    <div className="form3-picker-container">
      {isLoading && (
        <div className="loading-overlay">
          <div className="loading-spinner"></div>
        </div>
      )}
      <h3>* 어떤 상황인가요?</h3>
      <textarea
        placeholder="내용을 입력하세요"
        value={formData.situation}
        onChange={(e) =>
          setFormData((prev) => ({ ...prev, situation: e.target.value }))
        }
        className="form3-textarea"
      />

      <h3>원하는 답변</h3>
      <textarea
        placeholder="내용을 입력해주세요"
        value={formData.desiredAnswer}
        onChange={(e) =>
          setFormData((prev) => ({ ...prev, desiredAnswer: e.target.value }))
        }
        className="form3-textarea"
        style={{ height: "3rem" }}
      />

      <h3>언어</h3>
      <div className="tone-options">
        {["korean", "english", "japanese", "chinese", "french", "german"].map(
          (language) => (
            <button
              key={language}
              onClick={() => handleLanguageChange(language)}
              className={`tone-button ${
                selectedLanguage === language ? "tone-selected" : ""
              }`}
            >
              {language}
            </button>
          )
        )}
      </div>

      <h3>말투</h3>
      <div className="tone-options">
        {["격식있는", "친근한", "겸손한", "유머러스", "간결한", "논리적"].map(
          (tone) => (
            <button
              key={tone}
              onClick={() => handleToneChange(tone)}
              className={`tone-button ${
                formData.tone === tone ? "tone-selected" : ""
              }`}
            >
              {tone}
            </button>
          )
        )}
      </div>

      <button
        className="submit-button"
        onClick={handleSubmit}
        disabled={isLoading}
      >
        제출
      </button>
    </div>
  );
}
