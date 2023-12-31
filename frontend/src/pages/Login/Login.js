import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import StartHeader from "./../../components/Startheader/header";
import {
  InputSign,
  BackGround,
  InputBox,
  SmallButton,
  LoginContainer,
  GroundContainer,
  ButtonGroup,
  LoginSubject,
} from "./LoginStyle";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const onChangeEmail = (event) => {
    setEmail(event.target?.value);
  };
  const onChangePassword = (event) => {
    setPassword(event.target?.value);
  };
  const userLoginData = {
    email: email,
    password: password,
  };

  const onClickLogin = (e) => {
    e.preventDefault();
    console.log(email);
    console.log(password);
    if (!email) {
      return alert("이메일를 입력하세요.");
    } else if (!password) {
      return alert("비밀번호 입력하세요.");
    } else {
      fetch("http://localhost:8080/api/auth/signin", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userLoginData),
      })
        .then((response) => response.json())
        .then((response) => {
          console.log("결과: ", response);
          if (response.accessToken) {
            window.localStorage.setItem("accessToken", response.accessToken);
            window.localStorage.setItem("refreshToken", response.refreshToken);
            const token = localStorage.getItem("accessToken");
            axios
              .get("http://localhost:8080/api/auth/", {
                headers: { Authorization: `Bearer ${token}` },
              })
              .then((res) => {
                console.log(res);
                const info = res.data.information;
                return info;
              })
              .then((info) => {
                console.log(info);
                if (info.authority === "USER") {
                  if (info.floor === 0) {
                    alert("접근 불가능. 관리자로의 승인을 기다리는 중입니다.");
                    navigate("/");
                  } else {
                    navigate("/main/user/bill");
                  }
                } else {
                  navigate("/main/monitoring/energy_consumption");
                }
              });
          } else {
            alert(response.information.message);
          }
        })
        .catch((err) => alert(err));
    }
  };

  const pushSignuppage = (e) => {
    e.preventDefault();
    navigate("/signup");
  };

  return (
    <BackGround>
      <StartHeader />
      <GroundContainer>
        <LoginContainer>
          <LoginSubject>Login</LoginSubject>
          <InputBox>
            이메일:
            <InputSign type="text" onChange={onChangeEmail} value={email} />
          </InputBox>
          <InputBox>
            비밀번호:
            <InputSign
              type="password"
              onChange={onChangePassword}
              value={password}
            />
          </InputBox>
          <ButtonGroup>
            <SmallButton onClick={pushSignuppage}>Sign up</SmallButton>
            <SmallButton onClick={onClickLogin}>Log in</SmallButton>
          </ButtonGroup>
        </LoginContainer>
      </GroundContainer>
    </BackGround>
  );
}
