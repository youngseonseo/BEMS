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
import UserCouponPage from "./pages/Coupon/userCoupon/userCoupons";
import { useEffect, useState } from "react";
import UserSettingPage from "./pages/Setting/userSetting/userSetting";

function App() {
  const [isopened, setOpen] = useState(false);
  const [a_bus, setAbus] = useState();
  const [b_bus, setBbus] = useState();
  const [c_bus, setCbus] = useState();
  const token = localStorage.getItem("accessToken");
  const SSE = () => {
    const subscribeUrl = "http://localhost:8080/api/sub";

    if (token != null) {
      console.log("SSE ", token);
      let eventSource = new EventSourcePolyfill(subscribeUrl, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "text/event-stream",
        },
        heartbeatTimeout: 10000000,
        withCredentials: true,
      });

      eventSource.onopen = (event) => {
        console.log("this is opened", event);
        setOpen(true);
      };
      eventSource.addEventListener("addNotification", (event) => {
        alert(event.data);
      });
      eventSource.addEventListener("ess", (event) => {
        const json = JSON.parse(event.data);
        setAbus(json.a_bus);
        setBbus(json.b_bus);
        setCbus(json.c_bus);
      });
      eventSource.addEventListener("error", (e) => {
        console.log("An error occurred while attempting to connect.");
        setOpen(false);
      });
    }
  };
  useEffect(() => {
    SSE();
  }, [token]);

  console.log(a_bus, b_bus, c_bus);

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
        <Route path="/main/monitoring/battery" element={<BatteryPage />} />
        <Route
          path="/main/monitoring/power_distribution"
          element={
            <ElectricDistributionPage
              busA={a_bus}
              busB={b_bus}
              busC={c_bus}
              isopened={isopened}
            />
          }
        />

        <Route path="/main/user/bill" element={<FloorBillEnergyPage />} />
        <Route path="/main/user/coupon" element={<UserCouponPage />} />
        <Route path="/main/user/setting" element={<UserSettingPage />} />
        <Route path="/main/chatting" element={<ChattingPage />} />
        <Route path="/main/setting" element={<SettingPage />} />
        <Route path="/main/notify" element={<NorificationPage />} />
        <Route path="/main/coupon" element={<CouponPage />} />
      </Routes>
    </Router>
  );
}

export default App;
