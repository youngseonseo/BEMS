import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
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
import NotificationPage from "./pages/Notify/Notify";

function App() {
  const SSE = () => {};
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
          element={<ElectricDistributionPage />}
        />

        <Route path="/main/coupon" element={<CouponPage />} />
        <Route path="/main/chatting" element={<ChattingPage />} />
        <Route path="/main/setting" element={<SettingPage />} />
        <Route path="/main/notify" element={<NotificationPage />} />
      </Routes>
    </Router>
  );
}

export default App;
