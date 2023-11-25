import styled from "styled-components";
export const BackGround = styled.div`
  background-color: #292929;
  padding: 50px 50px;
  display: flex;
  gap: 30px;
  height: auto;
`;
export const MangementBackground = styled.div`
  display: flex;
  flex-direction: column;
  background-color: #404040;
  border-radius: 10px;
  height: auto;
  width: 70vw;
  color: white;
  padding: 20px;
  gap: 15px;
`;
export const Subtitle = styled.div`
  font-size: 18px;
  font-weight: bold;
  padding: 5px;
`;

export const ConsumptionPattern = styled.div`
  display: flex;
  background-color: #353535;
  justify-content: space-around;
  align-items: center;
  height: 300px;
  width: auto;
  color: white;
  padding: 10px;
  border-radius: 10px;
`;
export const ConsumptionPattern2 = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 300px;
  width: auto;
  color: white;
  padding: 10px;
  border-radius: 10px;
`;
export const DayGraph = styled.div`
  display: flex;
  background-color: #353535;
  height: auto;
  width: 400px;
  color: white;
  border-radius: 10px;
`;
export const TextPattern = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 20px;
  width: 350px;
  color: white;
`;

export const BatterySave = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  background-color: #353535;
  padding: 20px;
  border-radius: 10px;
  gap: 10px;
  width: 320px;
  height: 280px;
  color: white;
`;
export const CompareCost = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  background-color: #353535;
  padding: 20px;
  border-radius: 10px;
  gap: 5px;
  width: 650px;
  height: 280px;
  color: white;
`;
export const SaveBox = styled.div`
  background-color: white;
  color: black;
  border-radius: 10px;
  width: 250px;
  height: 40px;
  font-size: 25px;
  font-weight: bold;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 0px 30px;
`;
export const ColorChart = styled.div`
  background: rgb(54, 144, 69);
  background: linear-gradient(
    90deg,
    rgba(54, 144, 69, 1) 0%,
    rgba(255, 253, 79, 1) 50%,
    rgba(242, 54, 54, 1) 100%
  );
  border: 1px solid black;
  width: 400px;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto;
`;
export const Checker = styled.div`
  border: 1px solid black;
  width: 66.6px;
  height: 50px;
`;
export const PercentText = styled.div`
  width: 300px;
  display: flex;
  gap: 35px;
  align-items: center;
  margin: 0 auto;
`;
export const AvgPosition = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
`;

export const CheckPosition = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  left: ${(props) => `${props.post * 2}px`};
  position: relative;
  top: -10px;
  color: white;
`;
export const ImageRotate = styled.img`
  transform: rotate(-90deg);
`;
export const TextPercent = styled.div`
  background-color: white;
  padding: 5px;
  color: black;
  border-radius: 10px;
  width: 330x;
  height: 100px;
  font-size: 17px;
  font-weight: bold;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto;
`;
