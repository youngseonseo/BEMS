import styled from "styled-components";

export const LoginContainer = styled.div`
  font-family: inter;
  font-size: 16px;
  color: #fff;
  position: relative;
  bottom: 10px;
  left: -10px;

  /* Rectangle 20 */
  box-sizing: border-box;
  width: 400px;
  height: 300px;
  background: #302e2e;
  border: 2px solid rgba(245, 245, 245, 0.34);

  /* Auto layout */
  display: flex;
  flex-direction: column;
  padding: 25px 35px 12px;
  gap: 10px;
`;
export const GroundContainer = styled.div`
  margin: 0 auto;
  width: 400px;
  height: 300px;
  background: #302e2e;
  border: 2px solid rgba(245, 245, 245, 0.34);
`;

export const ButtonGroup = styled.div`
  display: flex;
  justify-content: center;
  padding: 20px 0px;
  gap: 20px;
`;

export const LoginSubject = styled.h1`
  display: flex;
  justify-content: center;
  padding-bottom: 40px;
`;

export const InputSign = styled.input`
  background-color: black;
  color: white;
  background: #000000;
  border: 2px solid #cbd2e0;
  border-radius: 6px;
  padding: 3px;
`;
export const InputBox = styled.div`
  display: flex;
  justify-content: space-between;
  padding: 1.5px;
`;

export const BackGround = styled.div`
  background-color: black;
  display: grid;

  width: 100vw;
  height: 100vh;
`;

export const SmallButton = styled.button`
  width: 70px;
  height: 35px;
  border-radius: 6px;
  border: 2px solid;
  background: black;
  color: white;
  &:hover {
    border: 3px solid black;
  }
`;

export const BigButton = styled.button`
  border-radius: 7px;
  border: 1px solid #c6c6c6;
  height: 35px;
  color: white;
  background: #d9d9d947;
  &:hover {
    border: 3px solid black;
  }
`;
