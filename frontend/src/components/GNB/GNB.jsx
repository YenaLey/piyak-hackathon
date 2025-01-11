import "./GNB.css";
import EditIcon from "@/image/gnbEdit.svg";
import HomeIcon from "@/image/gnbHome.svg";
import MailIcon from "@/image/gnbMail.svg";
import ProfileIcon from "@/image/gnbProfile.svg";
import SendIcon from "@/image/gnbSend.svg";

const GNB = () => {
  return (
    <div className="gnbContainer">
      <div className="gnbWrapper">
        <div className="gnbHeader">
          <span>Main Menu</span>
        </div>
        <div className="gnbBody">
          <button className="gnbBtn">
            <img src={HomeIcon} alt="Home" />
            <span>Home</span>
          </button>
          <button className="gnbBtn">
            <img src={ProfileIcon} alt="Profile" />
            <span>내 정보</span>
          </button>
          <button className="gnbBtn">
            <img src={EditIcon} alt="Edit" />
            <span>내 정보 수정</span>
          </button>
          <button className="gnbBtnLarge">
            <img src={SendIcon} alt="Send" />
            <span>메일 보내기</span>
          </button>
        </div>
      </div>
    </div>
  )
};

export default GNB;