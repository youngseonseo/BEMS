import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { EventSourcePolyfill } from "event-source-polyfill";
import "./App.css";
import StartPage from "./pages/Start/Startpage";
import LoginPage from "./pages/Login/Login";
import SignupPage from "./pages/Signup/Signup";
import EnergyConsumptionMonitoring from "./pages/Monitoring/energy/Energy";
import BatteryPage from "./pages/Monitoring/battery/Battery";
import ElectricDistributionPage from "./pages/Monitoring/distribution/Distribution";
import CouponPage from "./pages/Coupon/Coupons";
import SettingPage from "./pages/Setting/Setting";
import ChattingPage from "./pages/Chatting/Chatting";
import NorificationPage from "./pages/Notify/Notify";
import FloorBillEnergyPage from "./pages/Monitoring/energy/Floor_energy/Floor_energy";
import EnergyManagementUserPage from "./pages/Monitoring/energy/Energy_management/EnergyManagement";
import BuildingEnergyManagementPage from "./pages/Monitoring/energy/Energy_management/BuildingEnergyManagement";
import UserCouponPage from "./pages/Coupon/userCoupon/userCoupons";
import { useEffect, useState } from "react";
import UserSettingPage from "./pages/Setting/userSetting/userSetting";
import axios from "axios";

function App() {
  const [isopened, setOpen] = useState(false);
  const [data1, setData1] = useState([]);
  const [data2, setData2] = useState([]);
  const [data3, setData3] = useState([]);
  const [sendBattery, setSendBattery] = useState(false);

  const [a_bus, setAbus] = useState();
  const [b_bus, setBbus] = useState();
  const [c_bus, setCbus] = useState();
  const token = localStorage.getItem("accessToken");
  const SendingStart = () => {
    //eslint-disable-line no-unused-vars
    axios.post("http://localhost:8080/api/ess/monitor");
  };

  const ESSScheduling = () => {
    const token = localStorage.getItem("accessToken");
    const subscribeUrl = "http://localhost:8080/api/ess";

    if (token != null) {
      console.log("SSE ", token);
      let eventSourceess = new EventSourcePolyfill(subscribeUrl, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "text/event-stream",
        },
        heartbeatTimeout: 10 * 60 * 1000,
        withCredentials: true,
      });

      eventSourceess.onopen = (event) => {
        console.log("this is opened", event);

        //if (sendBattery === false && token != null) {
        //   SendingStart();
        // }
      };
      eventSourceess.addEventListener("essBatteryScheduling", (event) => {
        const json = JSON.parse(event.data);
        setSendBattery(true);
        setData1((prev) => [...prev, json.graph1]);
        setData2((prev) => [...prev, json.graph2]);
        setData3((prev) => [...prev, json.graph3]);
      });
      eventSourceess.addEventListener("getEssSchPrevData", (event) => {
        const json = JSON.parse(event.data);
        console.log(json);
        setData1(json.graph1);
        setData2(json.graph2);
        setData3(json.graph3);
      });

      eventSourceess.addEventListener("error", (e) => {
        console.log("An error occurred while attempting to connect.");
        eventSourceess.close();
      });
    }
  };
  const SSE = () => {
    const subscribeUrl = "http://localhost:8080/api/sub";

    if (token != null) {
      console.log("SSE ", token);
      let eventSource = new EventSourcePolyfill(subscribeUrl, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "text/event-stream",
        },
        heartbeatTimeout: 10 * 60 * 1000,
        withCredentials: true,
      });

      eventSource.onopen = (event) => {
        console.log("this is opened", event);
        setOpen(true);
        //if (sendBattery === false && token != null) {
        //   SendingStart();
        // }
      };
      eventSource.addEventListener("addNotification", (event) => {
        alert(event.data);
      });

      eventSource.addEventListener("ess", (event) => {
        const json = JSON.parse(event.data);
        console.log(json);
        setAbus(json.a_bus);
        setBbus(json.b_bus);
        setCbus(json.c_bus);
      });
      eventSource.addEventListener("error", (e) => {
        console.log("An error occurred while attempting to connect.");
        setOpen(false);
        eventSource.close();
      });
    }
  };
  useEffect(() => {
    SSE(); // eslint-disable-next-line react-hooks/exhaustive-deps
    SendingStart();
    if (sendBattery === false) {
      ESSScheduling();
      setSendBattery(true);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <Router>
      <Routes>
        {/*하나씩만 렌더링 하기 위함*/}
        <Route path="/" element={<StartPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />

        <Route
          path="/main/monitoring/energy_consumption"
          element={<EnergyConsumptionMonitoring />}
        />
        <Route
          path="/main/monitoring/battery"
          element={<BatteryPage data1={data1} data2={data2} data3={data3} />}
        />
        <Route
          path="/main/monitoring/power_distribution"
          element={
            <ElectricDistributionPage
              isopened={isopened}
              a_bus={a_bus}
              b_bus={b_bus}
              c_bus={c_bus}
            />
          }
        />

        <Route path="/main/user/bill" element={<FloorBillEnergyPage />} />
        <Route path="/main/user/coupon/:id" element={<UserCouponPage />} />
        {/*:뒤에 있는 이름의 파라미터가 변수를 받을 것이라고 인식*/}
        <Route path="/main/user/setting" element={<UserSettingPage />} />
        <Route
          path="/main/user/energymanagement"
          element={<EnergyManagementUserPage />}
        />

        <Route path="/main/chatting" element={<ChattingPage />} />
        <Route path="/main/setting" element={<SettingPage />} />
        <Route path="/main/notify" element={<NorificationPage />} />
        <Route path="/main/coupon" element={<CouponPage />} />
        <Route
          path="/main/buildingManagement"
          element={<BuildingEnergyManagementPage />}
        />
      </Routes>
    </Router>
  );
}

export default App;
