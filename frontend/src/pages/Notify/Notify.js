import NavigationBar from "../../components/NavBar/navbar";
import MainHeader from "../../components/MainHeader/header";
import { BackGround } from "./../Monitoring/energy/EnergyStyle";
import {
  NotiContainer,
  NotiTitle,
  NotiMessege,
  NotiOne1,
  NotiOne2,
  NotiAll,
  Subject,
  DeleteButton,
} from "./NotifiyStyle";
import axios from "axios";
import { useEffect, useState } from "react";

export default function NorificationPage() {
  const [oldnoti, SetOldnoti] = useState([]);
  const [newnoti, SetNewnoti] = useState([]);

  const accessToken = localStorage.getItem("accessToken");

  const getOldNoti = async () => {
    await axios
      .get("http://localhost:8080/api/notifications/old", {
        headers: { Authorization: `Bearer ${accessToken}` },
      })
      .then((res) => {
        console.log(res);
        SetOldnoti(res.data.notifications);
      });
  };

  const getNewNoti = async () => {
    await axios
      .get("http://localhost:8080/api/notifications/new", {
        headers: { Authorization: `Bearer ${accessToken}` },
      })
      .then((res) => {
        console.log(res);
        SetNewnoti(res.data.notifications);
      });
  };

  const DeleteAllnoti = () => {
    axios
      .delete("http://localhost:8080/api/notifications", {
        headers: { Authorization: `Bearer ${accessToken}` },
      })
      .then((res) => {
        console.log(res);
        alert("알림이 모두 삭제 되었습니다.");
      });
  };

  useEffect(() => {
    getOldNoti();
    getNewNoti();
  }, []);

  return (
    <div>
      <MainHeader />
      <BackGround>
        <NavigationBar name="notifying" />
        <NotiContainer>
          <NotiAll>
            <Subject>새로운 알림</Subject>
            {newnoti.map((noti) => (
              <NotiOne1>
                <img
                  src="/image/alert-circle.png"
                  width="30px"
                  height="30px"
                  alt="alert-circle"
                />
                <div>
                  <NotiTitle>{noti?.title}</NotiTitle>
                  <NotiMessege>{noti?.message}</NotiMessege>
                </div>
              </NotiOne1>
            ))}
          </NotiAll>
          <NotiAll>
            <Subject>읽은 알림</Subject>
            {oldnoti.map((noti) => (
              <NotiOne2>
                <img
                  src="/image/alert-circle.png"
                  width="30px"
                  height="30px"
                  alt="alert-circle"
                />
                <div>
                  <NotiTitle>{noti?.title}</NotiTitle>
                  <NotiMessege>{noti?.message}</NotiMessege>
                </div>
              </NotiOne2>
            ))}
          </NotiAll>
          <DeleteButton onClick={DeleteAllnoti}>알림 모두 삭제</DeleteButton>
        </NotiContainer>
      </BackGround>
    </div>
  );
}
