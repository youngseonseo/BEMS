import axios from "axios";
import { useEffect, useState } from "react";
import UserNavigationBar from "../../../components/NavBar/userNavbar";
import { useParams } from "react-router-dom";
import MainHeader from "../../../components/MainHeader/header";
import {
  BackGround,
  PageBackground,
  Coupon,
  AllCoupons,
  InfoOfCoupon,
  DeleteButton,
} from "./userCouponStyle";

export default function UserCouponPage() {
  const { id } = useParams();
  const token = localStorage.getItem("accessToken");
  const [coupons, setCoupons] = useState([]);
  const [userid, setUserId] = useState();
  const [auth, setAuth] = useState();
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
  const getCoupon = async () => {
    await axios
      .get(`http://localhost:8080/api/coupons/users/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => {
        setAuth(res.data.whoRequested);
        return res.data.myCoupons;
      })
      .then((data) => setCoupons(data));
  };
  const onClickDeleteCoupons = async (e) => {
    console.log(e.target.value);
    await axios
      .patch(`http://localhost:8080/api/coupons/${e.target.value}`, {})
      .then((res) => {
        alert("쿠폰 사용이 정상적으로 완료되었습니다!");
        window.location.replace(`/main/user/coupon/${id}`);
      });
  };
  useEffect(() => {
    getUserInfo();
    getCoupon();
  }, []);
  return (
    <div>
      <MainHeader />
      <BackGround>
        <UserNavigationBar name="coupon" />
        <PageBackground>
          <InfoOfCoupon>
            <div>
              P3 쿠폰: 에너지 소비량을 한 달 간 3% 감소시켰을 경우에 발급됩니다.
            </div>
            <div>
              P5 쿠폰: 에너지 소비량을 한 달 간 5% 감소시켰을 경우에 발급됩니다.
            </div>
            <div>쿠폰 사용은 관리 사무소에 문의 바랍니다.</div>
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
                  {auth === "USER" ? null : (
                    <DeleteButton
                      value={cpn.couponId}
                      onClick={onClickDeleteCoupons}
                    >
                      삭제
                    </DeleteButton>
                  )}
                </div>
              </Coupon>
            ))}
          </AllCoupons>
        </PageBackground>
      </BackGround>
    </div>
  );
}
