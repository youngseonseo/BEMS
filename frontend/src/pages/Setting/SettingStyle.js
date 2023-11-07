import styled from "styled-components";

export const DeleteButton = styled.button`
  background-color: #ffffff;
  opacity: 30%;
  width: 180px;
  height: 45px;
  border: 3px;
  border-radius: 5px;
  font-size: 15px;
  font-weight: bold;
  &:hover {
    border: 3px solid gray;
  }
`;

export const Managergroup = styled.div`
  width: 300px;
  display: grid;
  gap: 10px;
`;

export const ManagerButton = styled.button`
  background-color: #ffffff;
  opacity: 80%;
  width: 250px;
  height: 45px;
  border: 3px;
  border-radius: 5px;
  font-size: 15px;
  font-weight: bold;
  font-size: 15px;
  &:hover {
    border: 3px solid gray;
  }
`;

export const UserImg = styled.img`
  border-radius: 50%;
  overflow: hidden;
  background-color: white;
`;

export const SettingContainer = styled.div`
  display: flex;
  width: 100vw;
  gap: 40px;
  justify-content: space-around;
`;

export const ImageChangeDiv = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 0px 20px;
`;

export const ImgButton = styled.button`
  width: 180px;
  height: 45px;
  background-color: gray;
  border-radius: 10px;
  border: 3px;
  font-weight: bold;
  font-size: 15px;
  &:hover {
    border: 3px solid white;
  }
`;

export const CheckUserInfo = styled.div`
  width: 350px;
  height: 200px;
  color: white;

  background-color: gray;
  font-size: 15px;
  font-weight: bold;
  border-radius: 10px 10px 10px 0px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 5px;
`;

export const ButtonGroup = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: end;
`;

export const InfoManager = styled.div`
  color: #e16b6b;
  font-size: 13px;
`;
