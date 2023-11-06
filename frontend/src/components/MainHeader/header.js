import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import axios from "axios";
import { Header, UserInfo, UserImg, SmallButton } from "./headerStyle";
import { EventSourcePolyfill } from "event-source-polyfill";

export default function MainHeader() {
  const navigate = useNavigate();
  const [userInfo, setUserInfo] = useState([]);

  const logout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    navigate("/");
  };

  const getUserInfo = async () => {
    const accessToken = localStorage.getItem("accessToken");
    await axios
      .get("http://localhost:8080/api/auth/", {
        headers: { Authorization: `Bearer ${accessToken}` },
      })
      .then((res) => {
        console.log("in getUserInfo Function ", res);
        setUserInfo(res.data.information);
        setTimeout(getRefresh, 10000000);
      })
      .catch((err) => {
        //accesstoken 접근 불가 => refresh 토큰으로 갱신 필요
        console.log("cant access by access token", err);
        getRefresh();
        return;
      });
  };

  const getRefresh = async () => {
    //refresh 토큰을 갱신하는 경우
    const Refresh = localStorage.getItem("refreshToken");
    console.log("Get access token by refreshtoken", Refresh);
    await axios
      .post("http://localhost:8080/api/auth/refresh", {
        refreshToken: Refresh,
      })
      .then((res) => {
        console.log(res);
        localStorage.setItem("accessToken", res.data.accessToken);
        localStorage.setItem("refreshToken", res.data.refreshToken); //token 다시 local에 저장
      })
      .then(getUserInfo)
      .catch((err) => {
        //refresh token 도 만료된경우
        console.log("refresh 에서 어떤 오류가 난 건지 확인하기", err);
        // if (err.response.status === 400) {
        //   // 토큰들을 localstorage 에서 없앨지 말지 확인
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        // }
        navigate("/login"); // 로그인 페이지로 이동하여 재로그인 유도
      });
  };

  useEffect(() => {
    getUserInfo();
  }, []);

  const name = userInfo?.username;
  const img = userInfo?.imageUrl;
  //const authority = userInfo?.authority;

  return (
    <Header>
      <img src="/image/Big_Logo.png" width="200px" height="50px" alt="logo" />
      <UserInfo>
        <UserImg width={"38px"} height={"38px"} src={img} alt="userIMG" />
        <div>{name}</div>
        <SmallButton onClick={logout}>Log out</SmallButton>
      </UserInfo>
    </Header>
  );
}
