import styled from "styled-components";

export const BackGround = styled.div`
  background-color: #292929;
  padding: 50px 50px;
  display: flex;
  height: auto;
`;

export const Graph1 = styled.div`
  background-color: #353535;
  grid-column: 1 / 3;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;
export const Graph2 = styled.div`
  background-color: #353535;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;
export const Graph3 = styled.div`
  background-color: #353535;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

export const GraphContainer = styled.div`
  display: grid;
  grid-template-columns: repeat(2, 35vw);
  grid-template-rows: repeat(2, 350px);
  gap: 15px;
  color: white;
`;

export const EnergyContainer = styled.div`
  display: flex;
  flex-direction: column;
  padding: 0px 30px;
  gap: 15px;
`;

export const SelectBox = styled.select`
  width: 150px;
  height: 50px;
  border-radius: 10px;
  border: 3px solid white;
  color: white;
  font-size: 15px;
  background-color: #2d3648;
  margin-right: 0 auto;
`;
