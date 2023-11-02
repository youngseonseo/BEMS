import { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";

import {
  SignupContainer,
  InputSign,
  BackGround,
  InputBox,
  SmallButton,
  ButtonGroup,
  BigButton,
  SignupSubject,
  GroundContainer,
  ImageButton,
  Check,
} from "./SignupStyle";
import StartHeader from "./../../components/Startheader/header";

export default function SignupPage() {
  const [name, setName] = useState();
  const [email, setEmail] = useState();
  const [password, setPassword] = useState();
  const [passwordSame, setPasswordSame] = useState();
  const [imgFile, setImgFile] = useState(
    "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png"
  );

  const onChangeName = (event) => {
    setName(event.target.value);
  };
  const onChangeEmail = (event) => {
    setEmail(event.target.value);
  };
  const onChangePassword = (event) => {
    setPassword(event.target.value);
  };
  const onChangePasswordSame = (event) => {
    setPasswordSame(event.target.value);
  };

  const userData = {
    name: name,
    email: email,
    password: password,
    imageUrl: imgFile,
  };

  const registerFetch = (event) => {
    event.preventDefault();
    fetch("http://localhost:8080/api/auth/signup", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(userData),
    })
      .then((response) => response.json())
      .then((result) => {
        console.log("결과: ", result);
        if (result.check === true) {
          alert(result.information.message);
          navigate("/login");
        } else {
          alert(result.information.message);
        }
      });
  };

  const navigate = useNavigate();
  const pushLoginpage = (e) => {
    e.preventDefault();
    navigate("/login");
  };

  // 이미지 파일 gallary로 POST
  const ref = useRef(null);
  const onUploadButtonClick = (event) => {
    ref.current.click();
  };

  const readImage = async (event) => {
    console.log(event.target.files?.[0]);

    const formData = new FormData();
    formData.append("file", event.target.files?.[0]);
    console.log(formData);

    await fetch("http://localhost:8080/api/gallery", {
      method: "POST",
      headers: {}, // fetch 를 쓸때는 'Content-Type': 'multipart/form-data' 쓰지 않기, axios에서는 필요

      body: formData,
    })
      .then((response) => response.text())
      .then((result) => {
        console.log("image-url: ", result);
        setImgFile(result);
      });
  };

  return (
    <div>
      <BackGround>
        <StartHeader />
        <GroundContainer>
          <SignupContainer>
            <SignupSubject>SIGN UP</SignupSubject>
            <InputBox>
              <div>이름</div>
              <InputSign type="text" onChange={onChangeName} value={name} />
            </InputBox>
            <InputBox>
              <div>이메일</div>
              <InputSign type="text" onChange={onChangeEmail} value={email} />
            </InputBox>
            <InputBox>
              <div>비밀번호</div>
              <InputSign
                type="password"
                onChange={onChangePassword}
                value={password}
              />
            </InputBox>
            <InputBox>
              <div>비밀번호 확인</div>
              <InputSign
                type="password"
                onChange={onChangePasswordSame}
                value={passwordSame}
              />
            </InputBox>
            <Check>
              {password === passwordSame ? `` : `비밀번호가 일치하지 않습니다.`}
            </Check>
            <InputBox>
              프로필 이미지(선택):
              <InputSign
                hidden
                type="file"
                accept="image/*"
                multiple={false}
                ref={ref}
                onChange={readImage}
              />
              <ImageButton onClick={onUploadButtonClick}>
                이미지 업로드
              </ImageButton>
            </InputBox>

            <BigButton>네이버로 로그인</BigButton>
            <BigButton>카카오로 로그인</BigButton>
            <BigButton>구글로 로그인</BigButton>
            <ButtonGroup>
              <SmallButton onClick={registerFetch}>Sign up</SmallButton>
              <SmallButton onClick={pushLoginpage}>Log in</SmallButton>
            </ButtonGroup>
          </SignupContainer>
        </GroundContainer>
      </BackGround>
    </div>
  );
}
