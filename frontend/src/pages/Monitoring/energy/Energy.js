import NavigationBar from "../../../components/NavBar/navbar";
import MainHeader from "../../../components/MainHeader/header";
import { BackGround } from "./EnergyStyle";

export default function EnergyConsumptionMonitoring() {
  return (
    <div>
      <MainHeader />
      <BackGround>
        <NavigationBar name="energy_consumption" />
      </BackGround>
    </div>
  );
}
