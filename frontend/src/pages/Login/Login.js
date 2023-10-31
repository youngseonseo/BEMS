import { useState } from "react";
import { useNavigate } from "react-router-dom";
import StartHeader from "./../../components/Startheader/header";
import {
  InputSign,
  BackGround,
  InputBox,
  SmallButton,
  BigButton,
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
            navigate("/main/monitoring/energy_consumption");
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
          <BigButton>네이버로 로그인</BigButton>
          <BigButton>카카오로 로그인</BigButton>
          <BigButton>구글로 로그인</BigButton>
        </LoginContainer>
      </GroundContainer>
    </BackGround>
  );
}
