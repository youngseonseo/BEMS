import NavigationBar from "../../components/NavBar/navbar";
import MainHeader from "../../components/MainHeader/header";
import { BackGround } from "./../Monitoring/energy/EnergyStyle";

export default function NorificationPage() {
  return (
    <div>
      <MainHeader />
      <BackGround>
        <NavigationBar name="notifying" />
      </BackGround>
    </div>
  );
}
