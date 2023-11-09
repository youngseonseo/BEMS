import styled from "styled-components";

export const NotiContainer = styled.div`
  display: flex;
  justify-content: space-around;
  width: 100vw;
  padding: 15px;
`;

export const NotiTitle = styled.div`
  font-size: 20px;
  font-weight: bold;
`;

export const NotiMessege = styled.div`
  font-size: 15px;
`;

export const NotiOne1 = styled.div`
  width: 30vw;
  background-color: white;
  border-radius: 10px;
  border: 2px solid;
  display: flex;
  gap: 10px;
  padding: 10px;
`;

export const NotiOne2 = styled.div`
  width: 30vw;
  background-color: white;
  border-radius: 10px;
  border: 2px solid;
  display: flex;
  gap: 10px;
  padding: 10px;
  opacity: 0.5;
`;
export const NotiAll = styled.div`
  width: 30vw;
  display: flex;
  flex-direction: column;
  gap: 10px;
`;

export const Subject = styled.h1`
  color: white;
`;

export const DeleteButton = styled.button`
  color: white;
  background-color: #fa6055;
  width: 180px;
  height: 45px;
  font-size: 15px;
  font-weight: bold;
  border-radius: 10px;
`;
