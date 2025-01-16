import { useState, useEffect } from "react";
import "./Form4Picker.css";
import { sendEmail } from "@/services/axios";

export default function Form4Picker() {
  const initialData = (() => {
    const savedData = JSON.parse(localStorage.getItem("save-form4")) || {};
    return {
      id: savedData.id || 0,
      generatedTitle: savedData.generatedTitle || "",
      generatedContent: savedData.generatedContent || "",
      recipientMail: savedData.recipientMail || "",
      createdAt: savedData.createdAt || "",
      state: savedData.state || "",
    };
  })();

  const [generatedData, setGeneratedData] = useState(initialData);

  const handleSend = async () => {
    try {
      // 쿠키에서 accessToken 추출
      const accessToken = document.cookie
        .split("; ")
        .find((row) => row.startsWith("accessToken="))
        ?.split("=")[1];

      if (!accessToken) {
        alert("로그인 상태를 다시 확인해주세요.");
        return;
      }

      const submitForm = {
        accessToken,
        recipient: generatedData.recipientMail,
        subject: generatedData.generatedTitle,
        body: generatedData.generatedContent,
      };

      // 메일 전송 API 호출
      const sendEmailResponse = await sendEmail(submitForm);

      console.log("sendEmail 응답:", sendEmailResponse);

      if (sendEmailResponse.status === 200) {
        alert(`${generatedData.recipientMail}님께 메일이 전송되었습니다!`);
      } else {
        alert("메일 전송 중 문제가 발생했습니다.");
      }
    } catch (error) {
      console.error("메일 전송 중 오류 발생:", error);
      alert("메일 전송 중 오류가 발생했습니다.");
    }
  };

  useEffect(() => {
    const savedData = JSON.parse(localStorage.getItem("save-form4")) || {};
    setGeneratedData((prev) => ({
      ...prev,
      id: savedData.id || prev.id,
      generatedTitle: savedData.generatedTitle || prev.generatedTitle,
      generatedContent: savedData.generatedContent || prev.generatedContent,
      recipientMail: savedData.recipientMail || prev.recipientMail,
      createdAt: savedData.createdAt || prev.createdAt,
      state: savedData.state || prev.state,
    }));
  }, []);

  return (
    <div className="form4-picker-container">
      <h3>AI가 만든 이메일 결과</h3>
      <div className="generated-email">
        <div>
          <strong>{generatedData.generatedTitle}</strong>
        </div>
        <p className="recipient">
          받는 사람{" "}
          <span>
            {"<"}
            {generatedData.recipientMail}
            {">"}
          </span>
        </p>
        <div className="generated-content">
          {generatedData.generatedContent.split("\n").map((line, index) => (
            <p key={index}>{line}</p>
          ))}
        </div>
      </div>
      <div className="form4-button-box">
        {/* <button onClick={handleSave}>보관함 저장</button> */}
        <button onClick={handleSend}>메일 보내기</button>
      </div>
    </div>
  );
}
