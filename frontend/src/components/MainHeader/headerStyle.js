import styled from "styled-components";

export const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: white;
  background-color: black;
  height: 10vh;
  padding: 0px 70px;
`;
export const UserImg = styled.img`
  border-radius: 50%;
  overflow: hidden;
  background-color: white;
`;

export const UserInfo = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  font-size: 18px;
`;

export const SmallButton = styled.button`
  width: 80px;
  height: 35px;
  border-radius: 6px;
  border: 2px;
  background: #2d3648;
  color: white;
  font-size: 15px;
  &:hover {
    border: 3px solid gray;
  }
`;
