import styled from "styled-components";
export const BackGround = styled.div`
  background-color: #292929;
  padding: 50px 50px;
  display: flex;
  height: 90vh;
`;

export const PageBackground = styled.div`
  display: flex;
  flex-direction: column;
  padding: 0px 30px;
  gap: 15px;
`;

export const AllCoupons = styled.div`
  display: grid;
  grid-template-columns: repeat(3, 23vw);
  grid-template-rows: repeat(2, 130px);
  padding: 20px 0px;
  gap: 30px;
`;

export const Coupon = styled.div`
  width: 350px;
  height: 110px;

  background-image: ${(props) =>
    props.type === "P3"
      ? `url("/image/coupon_background_p3.png")`
      : `url("/image/coupon_background_p5.png")`};
  background-size: cover;
  display: flex;
  justify-content: space-around;
  align-items: center;
`;

export const InfoOfCoupon = styled.div`
  padding: 20px;
  border: 2px solid white;
  color: white;
  width: 700px;
  border-radius: 10px;
`;
