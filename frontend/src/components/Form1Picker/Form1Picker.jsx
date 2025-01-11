import { useState, useEffect, useCallback } from "react";
import useProfileStore from "@/hooks/useProfileStore";
import "./Form1Picker.css";

export default function Form1Picker() {
  const { profiles } = useProfileStore();

  const initialProfile = (() => {
    const savedDataString = localStorage.getItem("save-form1");
    if (savedDataString) {
      const savedData = JSON.parse(savedDataString);
      const profile = {
        name: savedData.name || "",
        job: savedData.job || "",
        affiliation: savedData.affiliation || "",
        number: savedData.number || "",
      };
      return profile;
    }
    return {
      name: "",
      job: "",
      affiliation: "",
      number: "",
    };
  })();

  const initialFields = (() => {
    const savedDataString = localStorage.getItem("save-form1");
    if (savedDataString) {
      const savedData = JSON.parse(savedDataString);
      return {
        name: !!savedData.name,
        job: !!savedData.job,
        affiliation: !!savedData.affiliation,
        number: !!savedData.number,
      };
    }
    return {
      name: false,
      job: false,
      affiliation: false,
      number: false,
    };
  })();

  const [selectedProfile, setSelectedProfile] = useState(initialProfile);
  const [selectedFields, setSelectedFields] = useState(initialFields);

  const handleCheckboxChange = (field) => {
    setSelectedFields((prev) => ({
      ...prev,
      [field]: !prev[field],
    }));
  };

  const handleSelectProfile = (profile) => {
    localStorage.removeItem("save-form1");
    setSelectedProfile(profile);
    setSelectedFields({
      name: false,
      job: false,
      affiliation: false,
      number: false,
    });
  };

  const saveDataToLocalStorage = useCallback(() => {
    const dataToSave = {};
    Object.keys(selectedFields).forEach((key) => {
      dataToSave[key] = selectedFields[key] ? selectedProfile[key] : null;
    });

    localStorage.setItem("save-form1", JSON.stringify(dataToSave));
  }, [selectedFields, selectedProfile]);

  useEffect(() => {
    return () => {
      saveDataToLocalStorage();
    };
  }, [saveDataToLocalStorage]);

  return (
    <div className="profile-select-container">
      <p>나는 누구인가요?</p>
      <div className="profile-select-options">
        {profiles.length > 0 ? (
          profiles.map((profile, index) => (
            <section
              key={index}
              onClick={() => handleSelectProfile(profile)}
              style={{
                backgroundColor:
                  selectedProfile === profile ? "#d4f7f9" : "#fff",
              }}
            >
              <div>
                <strong>이름</strong>
                <p
                  style={{
                    backgroundColor:
                      selectedProfile === profile ? "" : "whitesmoke",
                    color: selectedProfile === profile ? "" : "gray",
                  }}
                >
                  {profile.name || "정보 없음"}
                </p>
              </div>
              <div>
                <strong>직업</strong>
                <p
                  style={{
                    backgroundColor:
                      selectedProfile === profile ? "" : "whitesmoke",
                    color: selectedProfile === profile ? "" : "gray",
                  }}
                >
                  {profile.job || "정보 없음"}
                </p>
              </div>
              <div>
                <strong>소속</strong>
                <p
                  style={{
                    backgroundColor:
                      selectedProfile === profile ? "" : "whitesmoke",
                    color: selectedProfile === profile ? "" : "gray",
                  }}
                >
                  {profile.affiliation || "정보 없음"}
                </p>
              </div>
              <div>
                <strong>학번</strong>
                <p
                  style={{
                    backgroundColor:
                      selectedProfile === profile ? "" : "whitesmoke",
                    color: selectedProfile === profile ? "" : "gray",
                  }}
                >
                  {profile.number || "정보 없음"}
                </p>
              </div>
            </section>
          ))
        ) : (
          <p>저장된 프로필 정보가 없습니다.</p>
        )}
      </div>

      <h3>참고할 정보 선택하기</h3>
      <div className="profile-select-result">
        <label
          style={{
            backgroundColor: selectedFields.name ? "#d4f7f9" : "whitesmoke",
          }}
        >
          <input
            type="checkbox"
            checked={selectedFields.name}
            onChange={() => handleCheckboxChange("name")}
          />
          <p>{selectedProfile.name}</p>
        </label>
        <label
          style={{
            backgroundColor: selectedFields.job ? "#d4f7f9" : "whitesmoke",
          }}
        >
          <input
            type="checkbox"
            checked={selectedFields.job}
            onChange={() => handleCheckboxChange("job")}
          />
          <p>{selectedProfile.job}</p>
        </label>
        <label
          style={{
            backgroundColor: selectedFields.affiliation
              ? "#d4f7f9"
              : "whitesmoke",
          }}
        >
          <input
            type="checkbox"
            checked={selectedFields.affiliation}
            onChange={() => handleCheckboxChange("affiliation")}
          />
          <p>{selectedProfile.affiliation}</p>
        </label>
        <label
          style={{
            backgroundColor: selectedFields.number ? "#d4f7f9" : "whitesmoke",
          }}
        >
          <input
            type="checkbox"
            checked={selectedFields.number}
            onChange={() => handleCheckboxChange("number")}
          />
          <p>{selectedProfile.number}</p>
        </label>
      </div>
    </div>
  );
}
