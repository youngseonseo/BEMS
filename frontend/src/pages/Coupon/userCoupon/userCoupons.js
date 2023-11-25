import axios from "axios";
import { useEffect, useState } from "react";
import UserNavigationBar from "../../../components/NavBar/userNavbar";
import MainHeader from "../../../components/MainHeader/header";
import {
  BackGround,
  PageBackground,
  Coupon,
  AllCoupons,
  InfoOfCoupon,
} from "./userCouponStyle";

export default function UserCouponPage() {
  const token = localStorage.getItem("accessToken");
  const [coupons, setCoupons] = useState([]);

  const getCoupon = async () => {
    await axios
      .get("http://localhost:8080/api/coupon", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => {
        return res.data.myCoupons;
      })
      .then((data) => setCoupons(data));
  };
  useEffect(() => {
    getCoupon();
  }, []);
  console.log(coupons);
  return (
    <div>
      <MainHeader />
      <BackGround>
        <UserNavigationBar name="coupon" />
        <PageBackground>
          <InfoOfCoupon>
            <div>P3 쿠폰은 ------------------------------- 입니다.</div>
            <div>P5 쿠폰은 ------------------------------- 입니다.</div>
          </InfoOfCoupon>

          <AllCoupons>
            {coupons.map((cpn) => (
              <Coupon type={cpn.couponType}>
                <h1 style={{ position: "relative", left: "10px" }}>
                  {cpn.couponType}
                </h1>
                <div>
                  <div>발급일 : {cpn.issueDate}</div>
                  <div>유효기간 : {cpn.expirationDate}</div>
                </div>
              </Coupon>
            ))}
          </AllCoupons>
        </PageBackground>
      </BackGround>
    </div>
  );
}
