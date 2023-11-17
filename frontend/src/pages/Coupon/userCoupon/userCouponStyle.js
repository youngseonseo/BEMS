import styled from "styled-components";
export const BackGround = styled.div`
  background-color: #292929;
  padding: 50px 50px;
  display: flex;
  height: auto;
`;

export const PageBackground = styled.div`
  display: flex;
  flex-direction: column;
  padding: 0px 30px;
  gap: 15px;
`;

export const AllCoupons = styled.div`
  display: grid;
  grid-template-columns: repeat(3, 20vw);
  grid-template-rows: repeat(3, 130px);
  padding: 0px 30px;
  gap: 30px;
`;

export const Coupon = styled.div`
  width: 300px;
  height: 90px;
  background-image: url("/image/coupon_background_p3.png");
  background-size: cover;
  display: flex;
  justify-content: space-around;
  align-items: center;
`;
