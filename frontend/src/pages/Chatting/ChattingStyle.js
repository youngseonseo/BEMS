import styled from "styled-components";

export const ChattingContainer = styled.div`
  width: 70vw;
  height: 75vh;
  background-color: #404040;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  margin: 0px 20px;
`;
export const ChatWhite = styled.div`
  width: 70vw;
  height: 7vh;
  background-color: white;
  border-radius: 0px 0px 10px 10px;
  display: flex;
  justify-content: end;
  align-items: center;
  padding: 0px 20px;
  gap: 15px;
`;
export const ChatChat = styled.div`
  width: 70vw;
  height: 68vh;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  justify-content: end;
  gap: 10px;
  padding: 20px;
`;

export const ChatOne = styled.div`
  width: auto;
  height: 30px;
  display: flex;
  align-items: center;
  color: white;
  padding: 0px 20px;
  border-radius: 10px;
  align-self: flex-end;
  gap: 10px;
`;
export const ChatMes = styled.div`
  width: auto;
  height: 30px;
  display: flex;
  align-items: center;
  background-color: white;
  padding: 0px 20px;
  border-radius: 10px;
  align-self: flex-end;
  color: black;
  gap: 15px;
`;
export const SendButton = styled.button`
  width: 70px;
  height: 40px;
  border-radius: 10px;
  font-size: 15px;
  border: 3px solid gray;
  &:hover {
    border: 3px solid white;
  }
`;
