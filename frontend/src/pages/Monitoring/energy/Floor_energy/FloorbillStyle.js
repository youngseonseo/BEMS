import styled from "styled-components";
export const BackGround = styled.div`
  background-color: #292929;
  padding: 50px 50px;
  display: flex;
  gap: 30px;
  height: 90vh;
`;
export const GraphCont = styled.div`
  width: 70vw;
  display: flex;
  justify-content: space-around;
`;

export const TextCont = styled.div`
  width: 64vw;
  display: flex;
  padding: 10px 0px;
  justify-content: space-between;
`;
export const Graph1Cont = styled.div`
  background-color: #404040;
  width: 30vw;
  height: 58vh;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
`;
export const Graph2Cont = styled.div`
  width: 30vw;
  height: 58vh;
  background-color: #404040;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
`;
export const BillCont = styled.div`
  width: 70vw;
  height: 77vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-radius: 10px;
  color: white;
`;

export const ButtonPost = styled.button`
  width: 100px;
  height: 25px;
  background-color: black;
  border-radius: 5px;
  border: 2px solid white;
  &:hover {
    border: 3px solid gray;
  }
  color: white;
`;
