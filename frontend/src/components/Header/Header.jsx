import "./Header.css";
import { useNavigate } from "react-router-dom";
import Logo from "@/image/Logo.svg";
import { SocialLogin } from "@/components/SocialLogin/SocialLogin";

const Header = () => {
  const navigate = useNavigate();

  const handleLogoClick = () => {
    navigate("/");
  };

  return (
    <>
      <div id="header">
        <div className="headerLogo">
          <img src={Logo} alt="Logo" onClick={handleLogoClick} />
          <SocialLogin />
        </div>
      </div>
    </>
  );
};

export default Header;
