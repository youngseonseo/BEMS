import NavigationBar from "../../components/NavBar/navbar";
import MainHeader from "../../components/MainHeader/header";
import { BackGround } from "./../Monitoring/energy/EnergyStyle";
import { useEffect } from "react";
import axios from "axios";

export default function ChattingPage() {
  const accessToken = localStorage.getItem("accessToken");
  const intoChat = () => {
    axios
      .get("http://localhost:8080/chat/enter", {
        headers: { Authorization: `Bearer ${accessToken}` },
      })
      .then((res) => {
        console.log(res);
      });
  };

  useEffect(() => {
    intoChat();
  }, []);
  return (
    <div>
      <MainHeader />
      <BackGround>
        <NavigationBar name="chatting" />
      </BackGround>
    </div>
  );
}
