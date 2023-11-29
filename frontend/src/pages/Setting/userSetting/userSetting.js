import axios from "axios";
import { useEffect, useState, useRef } from "react";
import UserNavigationBar from "../../../components/NavBar/userNavbar";
import MainHeader from "../../../components/MainHeader/header";
import { BackGround } from "../../Monitoring/distribution/DistributionStyle";

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
} from "./../SettingStyle";

export default function UserSettingPage() {
  const [userInfo, setUserInfo] = useState([]);
  const [imgFile, setImgFile] = useState("");
  // 이미지 파일 gallary로 POST
  const ref = useRef(null);
  const onUploadButtonClick = (event) => {
    ref.current.click();
  };

  const readImage = async (event) => {
    console.log(event.target.files?.[0]);
    const accessToken = localStorage.getItem("accessToken");
    const formData = new FormData();
    formData.append("file", event.target.files?.[0]);
    console.log(formData);

    await fetch("http://localhost:8080/api/gallery", {
      method: "PUT",
      headers: { Authorization: `Bearer ${accessToken}` }, // fetch 를 쓸때는 'Content-Type': 'multipart/form-data' 쓰지 않기, axios에서는 필요
      body: formData,
    })
      .then((response) => response.text())
      .then((result) => {
        console.log("image-url: ", result);
        setImgFile(result);
      });
  };
  const applyManager = () => {
    const token = localStorage.getItem("accessToken");
    axios({
      method: "post",
      url: "http://localhost:8080/api/manager/apply",
      headers: { Authorization: `Bearer ${token}` },
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

  // const deleteMember = () => {
  //   const accessToken = localStorage.getItem("accessToken");
  //   axios({
  //     method: "delete",
  //     url: "http://localhost:8080/api/auth/",
  //     headers: { Authorization: `Bearer ${accessToken}` },
  //   })
  //     .then((res) => {
  //       console.log(res);
  //       alert("회원삭제");
  //     })
  //     .catch((err) => {
  //       console.log(err);
  //     });
  // };

  //const changeImage = () => {};

  //const onChangePassword = () => {};

  const getUserImage = () => {
    const token = localStorage.getItem("accessToken");
    axios
      .get("http://localhost:8080/api/auth/", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => {
        console.log("in getUserImage in UserSetting page", res);
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
        <UserNavigationBar name="setting" />

        <div
          style={{
            display: "flex",
            justifyContent: "space-around",
            width: "75vw",
          }}
        >
          <ImageChangeDiv>
            <UserImg
              src={userInfo?.imageUrl}
              width="200px"
              height="200px"
              alt="userImageSetting"
            />
            <input
              hidden
              type="file"
              accept="image/*"
              multiple={false}
              ref={ref}
              onChange={readImage}
            />
            <ImgButton onClick={onUploadButtonClick}>
              프로필 이미지 변경
            </ImgButton>
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
        </div>
      </BackGround>
    </div>
  );
}
