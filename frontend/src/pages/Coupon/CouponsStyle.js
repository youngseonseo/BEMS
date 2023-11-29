import styled from "styled-components";
export const ManagerCouponContainer = styled.div`
  display: flex;
  flex-direction: column;
  padding: 0px 30px;
  gap: 15px;
`;
export const CouponGetterCont = styled.div`
  display: flex;
  flex-direction: column;

  gap: 15px;
  width: 60vw;
  height: 50px;
  border-radius: 20px;
  background-color: #353535;
`;

export const CouponGetter = styled.div`
  display: flex;
  align-items: center;
  padding: 10px 40px;
  gap: 15px;
  width: 60vw;
  height: 50px;
  border-radius: 20px;
  background-color: #353535;
  color: white;
`;

export const GotoUserButton = styled.button`
  width: 80px;
  height: 35px;
  border-radius: 6px;
  border: 3px solid black;
  background: white;
  color: black;
  font-size: 18px;
  &:hover {
    border: 3px solid gray;
  }
  align-self: flex-end;
`;

export const Subtitle = styled.div`
  font-size: 18px;
  font-weight: bold;
  padding: 5px;
  color: white;
`;
