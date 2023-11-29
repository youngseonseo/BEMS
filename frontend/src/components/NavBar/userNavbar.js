import { useEffect, useState } from "react";
import { BigNavcoupon, BigNavsetting } from "./navbarStyle";

import { NavContainer, BigNavBill, BigNavManage } from "./userNavbarStyle";

import { useNavigate } from "react-router-dom";
import axios from "axios";

export default function UserNavigationBar(props) {
  const token = localStorage.getItem("accessToken");
  const [userid, setUserId] = useState();
  const getUserInfo = async () => {
    await axios
      .get("http://localhost:8080/api/auth/", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => {
        console.log(res.data);
        setUserId(res.data.information.id);
      });
  };
  const navigate = useNavigate();
  const onclickfloorbill = () => {
    navigate("/main/user/bill");
  };
  const onclickcoupon = () => {
    navigate(`/main/user/coupon/${userid}`);
  };
  const onclicksetting = () => {
    navigate("/main/user/setting");
  };
  const onclickEnergyManagement = () => {
    navigate("/main/user/energymanagement");
  };
  useEffect(() => {
    getUserInfo();
  }, []);
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
      <BigNavManage name={props.name} onClick={onclickEnergyManagement}>
        <img
          src="/image/monitoring.svg"
          width={"25px"}
          height={"25px"}
          alt="energy_management"
        />
        에너지 관리
      </BigNavManage>

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
