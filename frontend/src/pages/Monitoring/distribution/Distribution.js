import NavigationBar from "../../../components/NavBar/navbar";
import MainHeader from "../../../components/MainHeader/header";
import { useEffect, useState } from "react";
import { EventSourcePolyfill } from "event-source-polyfill";
import axios from "axios";
import {
  BackGround,
  BuildingLight,
  Buildingcontainer,
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
  ElectricImg,
} from "./DistributionStyle";

export default function ElectricDistributionPage({
  isopened,
  a_bus,
  b_bus,
  c_bus,
}) {
  const token = localStorage.getItem("accessToken");

  return (
    <div>
      <MainHeader />
      <BackGround>
        <NavigationBar name="electronic" />
        <DistBack>
          <TextBus1>BUS1</TextBus1>
          <Buildingcontainer>
            <ElectricImg>
              <img src="/image/Electric-bus.png" width={"150px"} />
              <img src="/image/Electric-bus.png" width={"150px"} />
            </ElectricImg>
            <Building>
              <BoxLine1_a bus={a_bus}></BoxLine1_a>
              <BuildingLight bus={a_bus}>
                <img
                  src="/image/building_dist.png"
                  width="200px"
                  height="200px"
                  alt="building"
                />
                <div>561동</div>
              </BuildingLight>

              <BoxLine2_a bus={a_bus}></BoxLine2_a>
            </Building>
            <Building>
              <BoxLine1_b bus={b_bus}></BoxLine1_b>
              <BuildingLight bus={b_bus}>
                <img
                  src="/image/building_dist.png"
                  width="200px"
                  height="200px"
                  alt="building"
                />
                <div>562동</div>
              </BuildingLight>
              <BoxLine2_b bus={b_bus}></BoxLine2_b>
            </Building>
            <Building>
              <BoxLine1_c bus={c_bus}></BoxLine1_c>
              <BuildingLight bus={c_bus}>
                <img
                  src="/image/building_dist.png"
                  width="200px"
                  height="200px"
                  alt="building"
                />
                <div>563동</div>
              </BuildingLight>
              <BoxLine2_c bus={c_bus}></BoxLine2_c>
            </Building>
          </Buildingcontainer>
          <TextBus2>BUS2</TextBus2>
        </DistBack>
      </BackGround>
    </div>
  );
}
