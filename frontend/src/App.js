import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import ProfileForm from "@/pages/ProfileForm";
import SendMail from "@/pages/SendMail";
import { Login, LoginRedirectHandler } from "@/pages/Login";
import Storage from "@/pages/Storage";
import Header from "@/components/Header/Header";
import Layout from "@/components/Layout/Layout";
import GNB from "@/components/GNB/GNB";

function App() {
  return (
    <Router>
      <Header />
      <div style={{display: "flex"}}>
        <GNB />
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/auth/callback" element={<LoginRedirectHandler />} />
          <Route path="/profile" element={<ProfileForm />} />
          <Route path="/send-mail" element={<SendMail />} />
          <Route path="/storage" element={<Storage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
