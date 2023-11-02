import styled from "styled-components";

export const DeleteButton = styled.button`
  font-size: 15px;
  color: white;
  background-color: #3c4859;
  width: 180px;
  height: 40px;
  border: 3px;
  border-radius: 5px;
  &:hover {
    border: 3px solid gray;
  }
`;

export const ManagerButton = styled.button`
  font-size: 15px;
  background-color: #2d65ba;
  color: white;
  width: 180px;
  height: 40px;
  border: 3px;
  border-radius: 5px;
  &:hover {
    border: 3px solid gray;
  }
`;

export const ButtonGroup = styled.div`
  display: flex;
  gap: 20px;
`;

export const UserImg = styled.img`
  border-radius: 50%;
  overflow: hidden;
  background-color: white;
`;

export const SettingContainer = styled.div`
  display: flex;
  gap: 20px;
`;

export const ImageChangeDiv = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
`;

export const ImgButton = styled.button`
  width: 180px;
  height: 45px;
  background-color: gray;
  border-radius: 10px;
  border: 3px;
  font-size: 15px;
  &:hover {
    border: 3px solid white;
  }
`;
