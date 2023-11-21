import NavigationBar from "../../components/NavBar/navbar";
import MainHeader from "../../components/MainHeader/header";
import { BackGround } from "./../Monitoring/distribution/DistributionStyle";
import {
  DeleteButton,
  ManagerButton,
  ButtonGroup,
  UserImg,
  SettingContainer,
  ImageChangeDiv,
  ImgButton,
  CheckUserInfo,
  InfoManager,
  Managergroup,
} from "./SettingStyle";
import axios from "axios";
import { useEffect, useState } from "react";

export default function SettingPage() {
  const [userInfo, setUserInfo] = useState([]);
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
    axios({
      method: "delete",
      url: "http://localhost:8080/api/auth/",
      headers: { Authorization: `Bearer ${accessToken}` },
    })
      .then((res) => {
        console.log(res);
        alert("회원삭제");
      })
      .catch((err) => {
        console.log(err);
      });
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
        console.log("in getUserImage in Setting pagge", res);
        setUserInfo(res.data.information);
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
              src={userInfo?.imageUrl}
              width="200px"
              height="200px"
              alt="userImageSetting"
            />
            <ImgButton>프로필 이미지 변경</ImgButton>
          </ImageChangeDiv>
          <CheckUserInfo>
            <div>이름 : {userInfo?.username}</div>
            <div>권한 : {userInfo?.authority}</div>
            <div>이메일 : {userInfo?.email}</div>
            <div> 동 : {userInfo?.building}</div>
            <div>호수 : {userInfo?.floor}</div>
          </CheckUserInfo>
          <ButtonGroup>
            <Managergroup>
              <ManagerButton onClick={applyManager}>
                건물 관리자 신청
              </ManagerButton>
              <InfoManager>
                건물 관리자일 경우 신청하기 버튼을 눌러주세요. 확인 후 7일 이내
                승인됩니다.
              </InfoManager>
            </Managergroup>

            <DeleteButton>회원 탈퇴</DeleteButton>
          </ButtonGroup>
        </SettingContainer>
      </BackGround>
    </div>
  );
}
