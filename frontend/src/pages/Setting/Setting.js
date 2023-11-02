import NavigationBar from "../../components/NavBar/navbar";
import MainHeader from "../../components/MainHeader/header";
import { BackGround } from "./../Monitoring/energy/EnergyStyle";
import {
  DeleteButton,
  ManagerButton,
  UserImg,
  SettingContainer,
  ImageChangeDiv,
  ImgButton,
} from "./SettingStyle";
import axios from "axios";
import { useEffect, useState } from "react";

export default function SettingPage() {
  const [userimg, setUserimg] = useState();
  const applyManager = () => {
    const accessToken = localStorage.getItem("accessToken");
    axios({
      method: "post",
      url: "http://localhost:8080/api/manager/apply",
      headers: { Authorization: `Bearer ${accessToken}` },
    })
      .then((res) => {
        console.log(res);
        alert("건물 관리자 신청이 완료되었습니다.");
      })
      .catch((err) => {
        console.log(err);
        alert("이미 건물 관리자 신청이 완료되었습니다. 승인을 기다려주세요");
      });
  };

  const deleteMember = () => {
    const accessToken = localStorage.getItem("accessToken");
  };

  const changeImage = () => {};

  const onChangePassword = () => {};

  const getUserImage = () => {
    const accessToken = localStorage.getItem("accessToken");
    axios
      .get("http://localhost:8080/api/auth/", {
        headers: { Authorization: `Bearer ${accessToken}` },
      })
      .then((res) => {
        console.log("in getUserImage in SEtting pagge", res);
        setUserimg(res.data.information.imageUrl);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  useEffect(() => {
    getUserImage();
  }, []);
  return (
    <div>
      <MainHeader />
      <BackGround>
        <NavigationBar name="setting" />
        <SettingContainer>
          <ImageChangeDiv>
            <UserImg
              src={userimg}
              width="200px"
              height="200px"
              alt="userImageSetting"
            />
            <ImgButton>프로필 이미지 변경하기</ImgButton>
          </ImageChangeDiv>
          <div>유저 정보 확인하기</div>
          <ManagerButton onClick={applyManager}>
            건물 관리자 신청하기
          </ManagerButton>
        </SettingContainer>
        <DeleteButton>회원 탈퇴하기</DeleteButton>
      </BackGround>
    </div>
  );
}
