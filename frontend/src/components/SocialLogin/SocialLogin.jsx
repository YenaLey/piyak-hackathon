import { useState, useEffect } from "react";
import { socialLogin } from "@/services/axios";
import useUserInfo from "@/hooks/useUserInfo";
import { FcGoogle } from "react-icons/fc";
import "./SocialLogin.css";

export function SocialLogin() {
  const [isLoggedIn, setIsLoggedIn] = useState(false); // 로그인 상태 관리
  const { setUserInfo } = useUserInfo();

  // 컴포넌트 마운트 시 쿠키를 통해 로그인 상태 확인
  useEffect(() => {
    // 쿠키에서 access token 존재 여부 확인
    const accessToken = document.cookie
      .split("; ")
      .find((row) => row.startsWith("accessToken="));

    if (accessToken) {
      setIsLoggedIn(true); // access token이 있으면 로그인 상태로 설정
    }
  }, []);

  // Google 로그인 시작
  const handleGoogleLogin = async () => {
    try {
      const { status, data } = await socialLogin("GOOGLE");
      if (status === 200) {
        window.location.href = data; // 소셜 로그인 URL로 리다이렉트
      }
    } catch (error) {
      console.error("Google Login Failed:", error);
      alert("Google 로그인 중 오류가 발생했습니다.");
    }
  };

  // 로그아웃 처리
  const handleLogout = () => {
    // 쿠키에서 access token 제거 (쿠키 만료 설정)
    document.cookie =
      "accessToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    setUserInfo(null, null);
    setIsLoggedIn(false); // 상태 초기화
  };

  // UI 렌더링
  return (
    <div className="social-login-button">
      {isLoggedIn ? (
        <button onClick={handleLogout}>로그아웃</button>
      ) : (
        <button onClick={handleGoogleLogin}>
          <FcGoogle className="social-login-button-icon" /> 구글로 시작하기
        </button>
      )}
    </div>
  );
}
