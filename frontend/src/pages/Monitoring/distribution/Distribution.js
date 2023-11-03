import NavigationBar from "../../../components/NavBar/navbar";
import MainHeader from "../../../components/MainHeader/header";
import { BackGround } from "./../energy/EnergyStyle";

export default function ElectricDistributionPage() {
  return (
    <div>
      <MainHeader />
      <BackGround>
        <NavigationBar name="electronic" />
      </BackGround>
    </div>
  );
}
