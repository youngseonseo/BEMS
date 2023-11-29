import styled from "styled-components";
export const BatteryBackground = styled.div`
  width: 75vw;
  height: auto;
  padding: 0px 40px;
  gap: 20px;
  display: flex;
  flex-direction: column;
`;

export const BatteryNow = styled.div`
  width: 200px;
  height: 300px;
  background-color: #353535;
  border-radius: 20px;
  color: white;
  padding: 14px;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
`;
export const InsideBattery = styled.div`
  width: 90px;
  height: 80px;
  background-color: #624bbf;
  border-radius: 15px;
`;
export const BatteryOutline = styled.div`
  width: 110px;
  height: 170px;
  border-radius: 20px;
  padding: 7px;
  border: 3px solid white;
  display: flex;
  align-items: end;
  justify-content: center;
`;
export const CustomBattery = styled.div`
  width: 50px;
  height: 7px;
  border-radius: 10px;
  border: 2px solid white;
  background-color: white;
`;

export const SOCGraph1 = styled.div`
  display: flex;
  width: 75vw;
  height: 320px;
  gap: 20px;
`;

export const SOCChange = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  width: 850px;
  height: 300px;
  background-color: #353535;
  border-radius: 20px;
  padding: 20px;
  color: white;
`;
export const BatteryGraph = styled.div`
  display: flex;
  flex-direction: column;
  width: 70vw;
  height: 320px;
  border-radius: 20px;
  background-color: #353535;
  padding: 20px;
  color: white;
  gap: 15px;
`;
