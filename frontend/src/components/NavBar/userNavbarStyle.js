import styled from "styled-components";

export const NavContainer = styled.div`
  width: 230px;
  height: 300px;
  border-radius: 10px;
  background-color: #6a6b6a;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  padding: 10px;
  gap: 13px;
`;

export const BigNavBill = styled.button`
  width: 200px;
  height: 50px;
  border-radius: 4px;
  border: ${(props) => (props.name === "bill" ? "3px solid white" : "#545454")};
  color: black;
  font-size: 18px;
  background: ${(props) => (props.name === "bill" ? "#c2c4c3" : "#545454")};
  display: flex;
  justify-content: start;
  align-items: center;
  padding: 0px 10px;
  gap: 8px;
  &:hover {
    border: 3px solid black;
  }
`;

export const BigNavManage = styled.button`
  width: 200px;
  height: 50px;
  border-radius: 4px;
  border: ${(props) =>
    props.name === "energy_management" ? "3px solid white" : "#545454"};
  color: black;
  font-size: 18px;
  background: ${(props) =>
    props.name === "energy_management" ? "#c2c4c3" : "#545454"};
  display: flex;
  justify-content: start;
  align-items: center;
  padding: 0px 10px;
  gap: 8px;
  &:hover {
    border: 3px solid black;
  }
`;
export const BigNavcoupon = styled.button`
  width: 200px;
  height: 50px;
  border-radius: 4px;
  border: ${(props) =>
    props.name === "coupon" ? "3px solid white" : "#545454"};
  color: black;
  font-size: 18px;
  background: ${(props) => (props.name === "coupon" ? "#c2c4c3" : "#545454")};
  display: flex;
  justify-content: start;
  align-items: center;
  padding: 0px 10px;
  gap: 8px;
  &:hover {
    border: 3px solid black;
  }
`;

export const BigNavsetting = styled.button`
  width: 200px;
  height: 50px;
  border-radius: 4px;
  border: ${(props) =>
    props.name === "setting" ? "3px solid white" : "#545454"};
  color: black;
  font-size: 18px;
  background: ${(props) => (props.name === "setting" ? "#c2c4c3" : "#545454")};
  display: flex;
  justify-content: start;
  align-items: center;
  padding: 0px 10px;
  gap: 8px;
  &:hover {
    border: 3px solid black;
  }
`;

export const BigNavnotifying = styled.button`
  width: 200px;
  height: 50px;
  border-radius: 4px;
  border: ${(props) =>
    props.name === "notifying" ? "3px solid white" : "#545454"};
  color: black;
  font-size: 18px;
  background: ${(props) =>
    props.name === "notifying" ? "#c2c4c3" : "#545454"};
  display: flex;
  justify-content: start;
  align-items: center;
  padding: 0px 10px;
  gap: 8px;
  &:hover {
    border: 3px solid black;
  }
`;
