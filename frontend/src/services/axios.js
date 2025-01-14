import axiosInstance from "./axiosConfig";

// 소셜 로그인 API 호출
export const socialLogin = async (socialLoginType) => {
  const response = await axiosInstance.get(`/auth/${socialLoginType}`);
  const { status, data } = response;
  console.log(data);
  return { status, data };
};

// AI에게 내용 보내기
export const sendForm = async ({ form }) => {
  const response = await axiosInstance.post(`api/generate-mail`, { form });
  const { status, data } = response;
  return { status, data };
};

// 생성된 메일 보내기
export const sendEmail = async ({ form }) => {
  const response = await axiosInstance.post(`/api/send-email`, { form });
  const { status, data } = response;
  return { status, data };
};
