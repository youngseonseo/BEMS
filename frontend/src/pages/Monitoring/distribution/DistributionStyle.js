import styled from "styled-components";

export const DistBack = styled.div`
  display: flex;
  flex-direction: column;
  padding: 20px;
  width: 100vw;
`;
export const Building = styled.div`
  width: 200px;
`;
export const BuildingLight = styled.div`
  height: 230px;
  width: 200px;
  background: ${(props) =>
    props.bus > 0
      ? `radial-gradient(
    circle,
    rgba(251, 255, 95, 1) 0%,
    rgba(255, 253, 79, 0) 56%)`
      : ""};
`;
export const Buildingcontainer = styled.div`
  display: flex;
  justify-content: space-around;
  align-items: center;
  color: white;
`;

export const BatteryImage = styled.img`
  transform: rotate(90deg);
  position: relative;
  z-index: 4;
`;
export const BoxLine1_a = styled.div`
  width: 23vw;
  height: 100px;
  border-right: ${(props) =>
    props.bus === 1 ? `7px solid yellow` : `7px solid black `};
  border-top: ${(props) =>
    props.bus === 1 ? `7px solid yellow` : `7px solid black `};
  position: relative;
  z-index: 1;
  top: -10px;
  right: 16vw;
`;
export const BoxLine1_b = styled.div`
  width: 37vw;
  height: 100px;
  border-right: ${(props) =>
    props.bus === 1 ? `7px solid yellow` : ` 7px solid black `};
  border-top: ${(props) =>
    props.bus === 1 ? `7px solid yellow` : ` 7px solid black `};
  position: relative;
  z-index: 2;
  top: -20px;
  right: 30.5vw;
`;
export const BoxLine1_c = styled.div`
  width: 53vw;
  height: 100px;
  border-right: ${(props) =>
    props.bus === 1 ? `7px solid yellow` : ` 7px solid black `};
  border-top: ${(props) =>
    props.bus === 1 ? `7px solid yellow` : `7px solid black `};
  position: relative;
  z-index: 3;
  top: -30px;
  right: 45.2vw;
`;
export const BoxLine2_a = styled.div`
  width: 23vw;
  height: 100px;
  border-right: ${(props) =>
    props.bus === 2 ? `7px solid yellow` : ` 7px solid black `};
  border-bottom: ${(props) =>
    props.bus === 2 ? `7px solid yellow` : ` 7px solid black `};
  position: relative;
  z-index: 1;
  top: -30px;
  right: 16vw;
`;
export const BoxLine2_b = styled.div`
  width: 37vw;
  height: 100px;
  border-right: ${(props) =>
    props.bus === 2 ? `7px solid yellow` : `7px solid black `};
  border-bottom: ${(props) =>
    props.bus === 2 ? `7px solid yellow` : ` 7px solid black `};
  position: relative;
  z-index: 2;
  top: -20px;
  right: 30.5vw;
`;
export const BoxLine2_c = styled.div`
  width: 53vw;
  height: 100px;
  border-right: ${(props) =>
    props.bus === 2 ? `7px solid yellow` : `7px solid black `};
  border-bottom: ${(props) =>
    props.bus === 2 ? `7px solid yellow` : `7px solid black  `};
  position: relative;
  z-index: 3;
  top: -10px;
  right: 45.2vw;
`;

export const OneBox = styled.div`
  width: 10vw;
  height: 12vh;
  border: 7px solid yellow;
  position: relative;
  z-index: 1;
  right: 10vw;
`;
export const TwoBox = styled.div`
  width: 6vw;
  height: 12vh;
  border-top: 7px solid yellow;
  position: relative;
  z-index: 1;
  top: 6vh;
  right: 12vw;
`;
export const ThreeBox = styled.div`
  width: 0vw;
  height: 63vh;
  border: 3.5px solid yellow;
  position: relative;
  z-index: 4;
  right: 14vw;
  top: -3vh;
`;
export const TextBus1 = styled.div`
  position: relative;
  top: -30px;
  margin: 0 auto;
  z-index: 4;
  font-size: 20px;
  color: white;
  font-weight: bold;
`;
export const TextBus2 = styled.div`
  position: relative;
  top: -10px;
  z-index: 4;
  font-size: 20px;
  color: white;
  margin: 0 auto;
  font-weight: bold;
`;
