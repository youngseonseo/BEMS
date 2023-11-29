import NavigationBar from "../../components/NavBar/navbar";
import MainHeader from "../../components/MainHeader/header";
import { BackGround } from "../Monitoring/energy/Floor_energy/FloorbillStyle";
import { useState, useEffect } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import {
  CouponGetter,
  ManagerCouponContainer,
  CouponGetterCont,
  GotoUserButton,
  Subtitle,
} from "./CouponsStyle";
export default function CouponPage() {
  const token = localStorage.getItem("accessToken");
  const [cpUser, setcpUser] = useState([]);
  const getCoupon = async () => {
    await axios
      .get("http://localhost:8080/api/coupons/managers")
      .then((res) => {
        setcpUser(res.data);
      });
  };
  useEffect(() => {
    getCoupon();
  }, []);
  return (
    <div>
      <MainHeader />
      <BackGround>
        <NavigationBar name="coupon" />
        <ManagerCouponContainer>
          <Subtitle>쿠폰을 가지고 있는 사람들 목록</Subtitle>
          <CouponGetterCont>
            {cpUser.map((user) => (
              <CouponGetter>
                <div>ID : {user.userId}</div>
                <div>이름 : {user.username}</div>
                <div>{user.building}동</div>
                <div>{user.floor}호</div>
                <GotoUserButton>
                  <Link to={`/main/user/coupon/${user.userId}`}>이동</Link>
                </GotoUserButton>
              </CouponGetter>
            ))}
          </CouponGetterCont>
        </ManagerCouponContainer>
      </BackGround>
    </div>
  );
}
