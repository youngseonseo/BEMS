import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "./App.css";
import StartPage from "./pages/Start/Startpage";
import LoginPage from "./pages/Login/Login";
import SignupPage from "./pages/Signup/Signup";
import EnergyConsumptionMonitoring from "./pages/Monitoring/energy/Energy";

function App() {
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
      </Routes>
    </Router>
  );
}

export default App;
