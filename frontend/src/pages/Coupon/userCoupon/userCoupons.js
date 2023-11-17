import axios from "axios";
import { useEffect, useState } from "react";
import UserNavigationBar from "../../../components/NavBar/userNavbar";
import MainHeader from "../../../components/MainHeader/header";
import {
  BackGround,
  PageBackground,
  Coupon,
  AllCoupons,
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
          <AllCoupons>
            {coupons.map((cpn) => (
              <Coupon>
                <h1>{cpn.couponType}</h1>
                <div>{cpn.expirationDate}</div>
              </Coupon>
            ))}
          </AllCoupons>
        </PageBackground>
      </BackGround>
    </div>
  );
}
