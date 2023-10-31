import styled from "styled-components";

export const BigNavmonitoring = styled.button`
  width: 200px;
  height: 50px;
  border-radius: 4px;
  border: #545454;
  color: black;
  font-size: 18px;
  background: #545454;
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
export const BigNavchatting = styled.button`
  width: 200px;
  height: 50px;
  border-radius: 4px;
  border: ${(props) =>
    props.name === "chatting" ? "3px solid white" : "#545454"};
  color: black;
  font-size: 18px;
  background: ${(props) => (props.name === "chatting" ? "#c2c4c3" : "#545454")};
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

export const NavContainer = styled.div`
  width: 230px;
  height: 500px;
  border-radius: 10px;
  background-color: #6a6b6a;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  padding: 10px;
`;

export const SmallNavconsumption = styled.button`
  width: 180px;
  height: 40px;
  border-radius: 4px;
  border: ${(props) =>
    props.name === "energy_consumption" ? "3px solid white" : "#868687"};
  color: black;
  font-size: 16px;
  background: ${(props) =>
    props.name === "energy_consumption" ? "#c2c4c3" : "#868687"};
  padding: 0px 10px;
  gap: 5px;
  display: flex;
  justify-content: start;
  align-items: center;
  &:hover {
    border: 3px solid black;
  }
`;
export const SmallNavbattery = styled.button`
  width: 180px;
  height: 40px;
  border-radius: 4px;
  border: ${(props) =>
    props.name === "battery" ? "3px solid white" : "#868687"};
  color: black;
  font-size: 16px;
  background: ${(props) => (props.name === "battery" ? "#c2c4c3" : "#868687")};
  padding: 0px 10px;
  gap: 5px;
  display: flex;
  justify-content: start;
  align-items: center;
  &:hover {
    border: 3px solid black;
  }
`;
export const SmallNavelectronic = styled.button`
  width: 180px;
  height: 40px;
  border-radius: 4px;
  border: ${(props) =>
    props.name === "electronic" ? "3px solid white" : "#868687"};
  color: black;
  font-size: 16px;
  background: ${(props) =>
    props.name === "electronic" ? "#c2c4c3" : "#868687"};
  padding: 0px 10px;
  gap: 5px;
  display: flex;
  justify-content: start;
  align-items: center;
  &:hover {
    border: 3px solid black;
  }
`;
export const SmallNavCont = styled.div`
  display: flex;
  flex-direction: column;
  gap: 5px;
  margin-top: -12px;
`;
