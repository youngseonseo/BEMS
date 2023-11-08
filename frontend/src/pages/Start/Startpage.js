import StartHeader from "./../../components/Startheader/header";
import {
  Subject,
  LayoutBlock,
  StartButton,
  BackGround,
} from "./StartpageStyle";

import React from "react";
import { useNavigate } from "react-router-dom";

export default function StartPage() {
  const navigate = useNavigate();

  const pushLoginpage = (e) => {
    e.preventDefault();
    navigate("/login");
  };

  return (
    <BackGround>
      <StartHeader />
      <LayoutBlock>
        <Subject>Managing the building Conveniently</Subject>
        <img
          src="./image/StartpageBuilding.png"
          layout="fixed"
          width="600px"
          height="600px"
          alt="building"
        />
      </LayoutBlock>
      <StartButton onClick={pushLoginpage}>Start</StartButton>
    </BackGround>
  );
}
