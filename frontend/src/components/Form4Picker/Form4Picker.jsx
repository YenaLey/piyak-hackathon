import { useState, useEffect } from "react";
import "./Form4Picker.css";
import { saveState, saveArchive } from "@/services/axios";

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

  const handleSave = async () => {
    try {
      // 첫 번째 API 호출: saveState
      const saveStateResponse = await saveState({
        form: {
          id: generatedData.id,
          generatedTitle: generatedData.generatedTitle,
          generatedContent: generatedData.generatedContent,
          recipientMail: generatedData.recipientMail,
          createdAt: generatedData.createdAt,
          state: "saved",
        },
      });

      console.log("saveState 응답:", saveStateResponse);

      // 두 번째 API 호출: saveArchive
      if (saveStateResponse.status === 200) {
        const form1 = JSON.parse(localStorage.getItem("save-form1")) || {};
        const form2 = JSON.parse(localStorage.getItem("save-form2")) || {};
        const form3 = JSON.parse(localStorage.getItem("save-form3")) || {};

        const saveArchiveResponse = await saveArchive({
          form: {
            form1,
            form2,
            form3,
            form4: {
              generatedTitle: generatedData.generatedTitle,
              generatedContent: generatedData.generatedContent,
            },
            state: "saved",
          },
        });

        console.log("saveArchive 응답:", saveArchiveResponse);

        if (saveArchiveResponse.status === 200) {
          alert("보관함에 저장되었습니다!");
        } else {
          alert("보관함 저장 중 문제가 발생했습니다.");
        }
      } else {
        alert("상태 저장 중 문제가 발생했습니다.");
      }
    } catch (error) {
      console.error("보관함 저장 중 오류 발생:", error);
      alert("보관함 저장 중 오류가 발생했습니다.");
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
      <div className="left-section">
        <h3>AI가 만든 이메일 결과</h3>

        <div className="generated-email">
          <div>
            <strong>{generatedData.generatedTitle}</strong>
          </div>
          <p className="recipient">
            받는 사람 <span>{"<"}{generatedData.recipientMail}{">"}</span>
          </p>
          <div className="generated-content">
            {generatedData.generatedContent.split("\n").map((line, index) => (
              <p key={index}>{line}</p>
            ))}
          </div>
        </div>
      </div>

      <button onClick={handleSave}>보관함 저장</button>
      <button>메일 보내기</button>
    </div>
  );
}
