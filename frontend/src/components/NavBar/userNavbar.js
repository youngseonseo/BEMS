import { BigNavcoupon, BigNavsetting } from "./navbarStyle";

import { NavContainer, BigNavBill } from "./userNavbarStyle";

import { useNavigate } from "react-router-dom";

export default function UserNavigationBar(props) {
  const navigate = useNavigate();
  const onclickfloorbill = () => {
    navigate("/main/user/bill");
  };
  const onclickcoupon = () => {
    navigate("/main/user/coupon");
  };
  const onclicksetting = () => {
    navigate("/main/user/setting");
  };

  return (
    <NavContainer>
      <BigNavBill name={props.name} onClick={onclickfloorbill}>
        <img
          src="/image/monitoring.svg"
          width={"25px"}
          height={"25px"}
          alt="monitoring"
        />
        고지서
      </BigNavBill>

      <BigNavcoupon name={props.name} onClick={onclickcoupon}>
        <img
          src="/image/coupon.png"
          width={"28px"}
          height={"27px"}
          alt="coupon"
        />
        쿠폰
      </BigNavcoupon>

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
