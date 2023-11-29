import styled from "styled-components";
export const BackGround = styled.div`
  display: flex;
  background-color: #292929;
  padding: 50px 50px;
  height: auto;
`;
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
  flex-direction: column;
  padding: 0px 40px;
  gap: 20px;
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
  width: 300px;
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

export const ManagerList = styled.div`
  display: flex;
  background-color: #353535;
  border-radius: 10px;
  color: white;
  align-items: center;

  padding: 0px 25px;
  height: 50px;
  width: 70vw;
  gap: 75px;
`;
export const ManagerEnroll = styled.div`
  display: flex;
  flex-direction: column;
  border-radius: 10px;
  color: white;
  align-items: center;
  padding: 0px 25px;
  gap: 20px;
`;

export const Enrollbutton = styled.button`
  width: 70px;
  height: 30px;
  border-radius: 10px;

  &:hover {
    border: 3px solid gray;
  }
`;
