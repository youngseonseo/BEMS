import NavigationBar from "../../../components/NavBar/navbar";
import MainHeader from "../../../components/MainHeader/header";
import { BackGround } from "./../energy/EnergyStyle";
import { useEffect, useState } from "react";
import axios from "axios";
import {
  BuildingLight,
  Buildingcontainer,
  BatteryImage,
  BoxLine1_a,
  BoxLine1_b,
  BoxLine1_c,
  BoxLine2_a,
  BoxLine2_b,
  BoxLine2_c,
  OneBox,
  TwoBox,
  ThreeBox,
  DistBack,
  Building,
  TextBus1,
  TextBus2,
} from "./DistributionStyle";

export default function ElectricDistributionPage({
  busA,
  busB,
  busC,
  isopened,
}) {
  console.log("this is opened : ", isopened);
  const postESS = () => {
    const accessToken = localStorage.getItem("accessToken");
    axios.post("http://localhost:8080/api/ess/monitor", {
      headers: { Authorization: `Bearer ${accessToken}` },
    });
  };

  useEffect(() => {
    postESS();
  }, [isopened]);

  return (
    <div>
      <MainHeader />
      <BackGround>
        <NavigationBar name="electronic" />
        <DistBack>
          <TextBus1>BUS1</TextBus1>
          <Buildingcontainer>
            <BatteryImage
              src="/image/battery-elec.png"
              width="150px"
              height="150px"
              alt="battery"
            />
            <OneBox></OneBox>
            <TwoBox></TwoBox>
            <ThreeBox></ThreeBox>
            <Building>
              <BoxLine1_a bus={busA}></BoxLine1_a>
              <BuildingLight bus={busA}>
                <img
                  src="/image/building_dist.png"
                  width="200px"
                  height="200px"
                  alt="building"
                />
                <div>561동</div>
              </BuildingLight>

              <BoxLine2_a bus={busA}></BoxLine2_a>
            </Building>
            <Building>
              <BoxLine1_b bus={busB}></BoxLine1_b>
              <BuildingLight bus={busB}>
                <img
                  src="/image/building_dist.png"
                  width="200px"
                  height="200px"
                  alt="building"
                />
                <div>562동</div>
              </BuildingLight>
              <BoxLine2_b bus={busB}></BoxLine2_b>
            </Building>
            <Building>
              <BoxLine1_c bus={busC}></BoxLine1_c>
              <BuildingLight bus={busC}>
                <img
                  src="/image/building_dist.png"
                  width="200px"
                  height="200px"
                  alt="building"
                />
                <div>563동</div>
              </BuildingLight>
              <BoxLine2_c bus={busC}></BoxLine2_c>
            </Building>
          </Buildingcontainer>
          <TextBus2>BUS2</TextBus2>
        </DistBack>
      </BackGround>
    </div>
  );
}
