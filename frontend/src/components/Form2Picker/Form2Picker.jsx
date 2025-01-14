import { useState, useEffect, useCallback } from "react";
import "./Form2Picker.css";

function RecipientInputField({ label, type, value, required, onChange }) {
  return (
    <div className="recipientInputField">
      <label className={required ? "labelRequired" : ""}>{label}</label>
      <input
        type={type}
        value={value}
        required={required}
        onChange={onChange}
        placeholder={`${label}을 입력해주세요`}
      />
    </div>
  );
}

export default function Form2Picker() {
  const initialRecipientData = (() => {
    const savedDataString = localStorage.getItem("save-form2");
    if (savedDataString) {
      const savedData = JSON.parse(savedDataString);
      return {
        recipientName: savedData.recipientName || "",
        recipientMail: savedData.recipientMail || "",
      };
    }
    return {
      recipientName: "",
      recipientMail: "",
    };
  })();

  const [recipientData, setRecipientData] = useState(initialRecipientData);

  const saveDataToLocalStorage = useCallback(() => {
    localStorage.setItem("save-form2", JSON.stringify(recipientData));
    console.log("save-form2에 데이터가 저장되었습니다.", recipientData);
  }, [recipientData]);

  const handleChange = (field, value) => {
    setRecipientData((prev) => ({ ...prev, [field]: value }));
  };

  useEffect(() => {
    return () => {
      saveDataToLocalStorage();
    };
  }, [saveDataToLocalStorage]);

  return (
    <div id="recipientPicker">
      <title>
        <p>상대방은 누구인가요?</p>
      </title>
      <section>
        <div>
          <RecipientInputField
            label="이름"
            type="text"
            value={recipientData.recipientName}
            required={true}
            onChange={(e) => handleChange("recipientName", e.target.value)}
          />
          <RecipientInputField
            label="메일주소"
            type="email"
            value={recipientData.recipientMail}
            required={true}
            onChange={(e) => handleChange("recipientMail", e.target.value)}
          />
        </div>
      </section>
    </div>
  );
}
