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

export default function ElectricDistributionPage({ isopened }) {
  const [a_bus, setAbus] = useState();
  const [b_bus, setBbus] = useState();
  const [c_bus, setCbus] = useState();
  const token = localStorage.getItem("accessToken");
  const SSE = () => {
    const subscribeUrl = "http://localhost:8080/api/sub";

    if (token != null) {
      console.log("SSE ", token);
      let eventSource2 = new EventSourcePolyfill(subscribeUrl, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "text/event-stream",
        },
        heartbeatTimeout: 10 * 60 * 1000,
        withCredentials: true,
      });

      eventSource2.onopen = (event) => {
        console.log("this is opened", event);
      };
      eventSource2.addEventListener("ess", (event) => {
        const json = JSON.parse(event.data);

        setAbus(json.a_bus);
        setBbus(json.b_bus);
        setCbus(json.c_bus);
      });
      eventSource2.addEventListener("error", (e) => {
        console.log("An error occurred while attempting to connect.");
        eventSource2.close();
      });
    }
  };

  const postESS = () => {
    const accessToken = localStorage.getItem("accessToken");
    axios.post("http://localhost:8080/api/ess/monitor");
  };

  useEffect(() => {
    SSE();
  }, [token]);

  useEffect(() => {
    postESS();
  }, []);

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
