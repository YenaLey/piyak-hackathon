import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import useProfileStore from "@/hooks/useProfileStore";
import useUserInfo from "@/hooks/useUserInfo";
import "@/styles/ProfileForm.css";
import ProfileIcon from "@/image/profile.png";

export default function ProfileForm() {
  const [searchParams] = useSearchParams();
  const { profiles, addProfile } = useProfileStore(); // 새로운 프로필 추가 함수 가져오기
  const { name, email, setUserInfo } = useUserInfo();

  const initialProfile = {
    name: "",
    job: "",
    affiliation: "",
    number: "",
  };

  const [formData, setFormData] = useState(initialProfile);

  useEffect(() => {
    console.log(profiles);
  }, [profiles]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (
      !formData.name ||
      !formData.job ||
      !formData.affiliation ||
      !formData.number
    ) {
      alert("모든 필드를 채워주세요!");
      return;
    }
    addProfile(formData);
    setFormData(initialProfile);
    alert("새로운 프로필이 저장되었습니다.");
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  useEffect(() => {
    // URL에서 name과 email 추출
    const nameParam = searchParams.get("name");
    const emailParam = searchParams.get("email");

    // Zustand에 저장
    if (nameParam && emailParam) {
      setUserInfo(
        decodeURIComponent(nameParam),
        decodeURIComponent(emailParam)
      );
    }
  }, [searchParams, setUserInfo]);

  return (
    <div className="profile-form-container">
      <div className="profile-form-header">MY</div>
      <div className="profile-form-body">
        <div className="profile-form-body-top">
          <img src={ProfileIcon} alt="Profile" />
          <div className="profile-form-body-top-text">
            <div className="profile-form-body-top-text-info">내 정보</div>
            <div className="profile-form-body-top-text-name">
              <p>이름</p>
              <span>{name}</span>
              <p>이메일</p>
              <span>{email}</span>
            </div>
          </div>
        </div>
        <div className="profile-form-body-bottom">
          <div className="profile-form-body-bottom-header">
            <span>Persona</span>
            <p>나만의 정보를 자유롭게 만드세요!</p>
          </div>
          <div className="profile-form-body-bottom-form">
            <Form
              header="Persona"
              handleSubmit={handleSubmit}
              handleChange={handleChange}
              formData={formData}
            />
            {profiles.length > 0 ? (
              profiles.map((profile, index) => (
                <section className="persona-block" key={index}>
                  <strong>이름</strong>
                  <div>
                    <p>{profile.name || "정보 없음"}</p>
                  </div>
                  <strong>직업</strong>
                  <div>
                    <p>{profile.job || "정보 없음"}</p>
                  </div>
                  <strong>소속</strong>
                  <div>
                    <p>{profile.affiliation || "정보 없음"}</p>
                  </div>
                  <strong>학번</strong>
                  <div>
                    <p>{profile.number || "정보 없음"}</p>
                  </div>
                </section>
              ))
            ) : (
              <p>저장된 페르소나 정보가 없습니다.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

function Form({ header, handleSubmit, handleChange, formData }) {
  return (
    <form className="profile-form-footer" onSubmit={handleSubmit}>
      <div className="profile-form-footer-header">{header}</div>
      <div className="profile-form-footer-body">
        <div className="profile-form-footer-body-item">
          <label htmlFor="name">이름</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
          />
        </div>
        <div className="profile-form-footer-body-item">
          <label htmlFor="job">직급</label>
          <input
            type="text"
            name="job"
            value={formData.job}
            onChange={handleChange}
            required
          />
        </div>
        <div className="profile-form-footer-body-item">
          <label htmlFor="affiliation">소속</label>
          <input
            type="text"
            name="affiliation"
            value={formData.affiliation}
            onChange={handleChange}
            required
          />
        </div>
        <div className="profile-form-footer-body-item">
          <label htmlFor="number">학번</label>
          <input
            type="text"
            name="number"
            value={formData.number}
            onChange={handleChange}
            required
          />
        </div>
      </div>
      <button className="save-button" type="submit">
        저장
      </button>
    </form>
  );
}
