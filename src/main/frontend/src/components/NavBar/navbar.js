import {
  BigNavmonitoring,
  NavContainer,
  SmallNavconsumption,
  SmallNavCont,
  SmallNavbattery,
  SmallNavelectronic,
  BigNavcoupon,
  BigNavchatting,
  BigNavsetting,
  BigNavnotifying,
} from "./navbarStyle";

import { useNavigate } from "react-router-dom";

export default function NavigationBar(props) {
  const navigate = useNavigate();
  const onclickenergyconsumption = () => {
    navigate("/main/monitoring/energy_consumption");
  };
  const onclickbattery = () => {
    navigate("/main/monitoring/battey");
  };
  const onclickelectonic = () => {
    navigate("/main/monitoring/power_distribution");
  };
  const onclickcoupon = () => {
    navigate("/main/coupon");
  };
  const onclickchatting = () => {
    navigate("/main/chatting");
  };
  const onclicksetting = () => {
    navigate("/main/setting");
  };
  const onclicknotify = () => {
    navigate("/main/notify");
  };
  return (
    <NavContainer>
      <BigNavmonitoring name={props.name} onClick={onclickenergyconsumption}>
        <img
          src="/image/monitoring.svg"
          width={"25px"}
          height={"25px"}
          alt="monitoring"
        />
        모니터링
      </BigNavmonitoring>
      <SmallNavCont>
        <SmallNavconsumption
          name={props.name}
          onClick={onclickenergyconsumption}
        >
          <img
            src="/image/energy-consumption.svg"
            width={"25px"}
            height={"25px"}
            alt="eneryconsumption"
          />
          에너지 소비
        </SmallNavconsumption>
        <SmallNavbattery name={props.name} onClick={onclickbattery}>
          <img
            src="/image/battery.svg"
            width={"25px"}
            height={"25px"}
            alt="battery"
          />
          배터리 스케줄링
        </SmallNavbattery>
        <SmallNavelectronic name={props.name} onClick={onclickelectonic}>
          <img
            src="/image/electronic.png"
            width={"20px"}
            height={"20px"}
            alt="elecDistribution"
          />
          전력량분배
        </SmallNavelectronic>
      </SmallNavCont>

      <BigNavcoupon name={props.name} onClick={onclickcoupon}>
        <img
          src="/image/coupon.png"
          width={"28px"}
          height={"27px"}
          alt="coupon"
        />
        쿠폰
      </BigNavcoupon>
      <BigNavnotifying name={props.name} onClick={onclicknotify}>
        <img
          src="/image/BellRinging.png"
          width={"25px"}
          height={"25px"}
          alt="notification"
        />
        알림
      </BigNavnotifying>
      <BigNavchatting name={props.name} onClick={onclickchatting}>
        <img
          src="/image/Chats.png"
          width={"25px"}
          height={"25px"}
          alt="chatting"
        />
        채팅
      </BigNavchatting>
      <BigNavsetting name={props.name} onClick={onclicksetting}>
        <img
          src="/image/setting.png"
          width={"25px"}
          height={"25px"}
          alt="setting"
        />
        설정
      </BigNavsetting>
    </NavContainer>
  );
}
