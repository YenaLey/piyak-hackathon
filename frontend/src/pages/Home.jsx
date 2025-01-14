import Background from "@/image/Background.png";

const Home = () => {
  return (
    <div className="home-wrapper" style={{ width: "100%", height: "100%" }}>
      <img
        src={Background}
        alt="Background"
        style={{ width: "100%", height: "100%" }}
      />
    </div>
  );
};

export default Home;
