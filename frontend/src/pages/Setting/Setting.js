import NavigationBar from "../../components/NavBar/navbar";
import MainHeader from "../../components/MainHeader/header";
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
  ManagerList,
  ManagerEnroll,
  BackGround,
  Enrollbutton,
} from "./SettingStyle";
import axios from "axios";
import { useEffect, useRef, useState } from "react";

export default function SettingPage() {
  const [userInfo, setUserInfo] = useState([]);
  const [applylist, setApplyList] = useState([]);
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

  const getManagerApplyList = () => {
    const accessToken = localStorage.getItem("accessToken");
    axios
      .get("http://localhost:8080/api/manager/applyList", {
        headers: { Authorization: `Bearer ${accessToken}` },
      })
      .then((res) => {
        console.log("Applylist", res);
        setApplyList(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  };
  const onClickEnroll = (e) => {
    const accessToken = localStorage.getItem("accessToken");
    axios
      .post(
        `http://localhost:8080/api/manager/enroll?managerId=${e.target.value}`,
        {},
        { headers: { Authorization: `Bearer ${accessToken}` } }
      )
      .then((res) => {
        console.log("EnrollManager", res);
        alert(res.data.information.message);
        window.location.replace(`/main/setting`);
      })
      .catch((err) => {
        console.log(err);
      });
  };
  useEffect(() => {
    getUserImage();
    getManagerApplyList();
  }, []);

  return (
    <div>
      <MainHeader />
      <BackGround>
        <NavigationBar name="setting" />

        <SettingContainer>
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
                width="180px"
                height="180px"
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
            <DeleteButton>회원 탈퇴</DeleteButton>
          </div>
          <div>
            {userInfo?.authority === "ADMIN" ? (
              <ManagerEnroll>
                <div style={{ fontSize: "20px", fontWeight: "bold" }}>
                  관리자 신청 현황
                </div>
                {applylist.map((mlist) => (
                  <ManagerList>
                    <div>ID : {mlist.memberId}</div>
                    <div>이름 : {mlist.memberName}</div>
                    <div>EMAIL : {mlist.memberEmail}</div>
                    <Enrollbutton
                      value={mlist.managerId}
                      onClick={onClickEnroll}
                    >
                      등록
                    </Enrollbutton>
                  </ManagerList>
                ))}
              </ManagerEnroll>
            ) : null}
          </div>
        </SettingContainer>
      </BackGround>
    </div>
  );
}
