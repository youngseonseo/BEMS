import NavigationBar from "../../components/NavBar/navbar";
import MainHeader from "../../components/MainHeader/header";
import { BackGround } from "./../Monitoring/energy/Floor_energy/FloorbillStyle";
import {
  ChatChat,
  ChatMes,
  ChatOne,
  ChattingContainer,
  ChatWhite,
  SendButton,
} from "./ChattingStyle";
import { useEffect, useRef, useState } from "react";
import axios from "axios";

export default function ChattingPage() {
  const [intoChatdata, setIntoChatdata] = useState(); //eslint-disable-line no-unused-vars
  const [message, setMessage] = useState(""); //메세지 유저 및 내용
  const [chatList, setChatList] = useState([]); //eslint-disable-line no-unused-vars
  const [senderinfo, setSenderinfo] = useState([]);
  const onChange = (event) => setMessage(event.target.value);
  const moment = require("moment");

  const sentTime = moment();
  const roomId = "83fdc206-23c2-4665-9ad6-5f638d2a9f3d";

  const content = message;
  const intoChat = async () => {
    const accessToken = localStorage.getItem("accessToken");
    await axios
      .get(
        "http://localhost:8080/api/chatrooms/83fdc206-23c2-4665-9ad6-5f638d2a9f3d",
        {
          headers: { Authorization: `Bearer ${accessToken}` },
        }
      )
      .then((res) => {
        console.log(res);
        setChatList(res.data);
      });
  };
  const getUserInfo = async () => {
    const accessToken = localStorage.getItem("accessToken");
    await axios
      .get("http://localhost:8080/api/auth/", {
        headers: { Authorization: `Bearer ${accessToken}` },
      })
      .then((res) => {
        console.log("in getUserInfo Function ", res);
        setSenderinfo(res.data.information);
      });
  };
  const url = "ws://localhost:8080/ws";
  const socket = useRef(null);

  useEffect(() => {
    getUserInfo();
    intoChat();
    /* 서버와의 websocket 연결 요청 */
    if (!socket.current) {
      socket.current = new WebSocket(url);
      socket.current.onopen = () => {
        console.log("서버와의 websocket 연결이 성공적으로 이뤄졌습니다!");
      };

      /* 서버에서 보낸 데이터 수신 */
      socket.current.onmessage = (event) => {
        console.log("chat message from server:", event.data);
        if (!event.data.includes("입장")) {
          const json = JSON.parse(event.data);
          setChatList((prev) => [...prev, json]);
        } else {
          alert(event.data);
        }
      };
      /* 서버와의 websocket 연결 종료 */
      socket.current.onclose = () => {
        console.log("서버와의 websocket 연결이 종료되었습니다!");
      };
      /* websocket 연결에 오류 발생 */
      socket.current.onerror = (error) => {
        console.log("websocket 연결에 오류가 발생했습니다:", error);
      };
    }
    return; // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  const onClicksendMessage = (event) => {
    event.preventDefault();
    if (socket.current.readyState === WebSocket.OPEN) {
      const userId = senderinfo.id;
      const username = senderinfo.username;

      const chatMsg = {
        chatRoom: {
          roomId: roomId,
        },
        type: "TALK",
        sender: {
          id: userId,
          username: username,
        },
        content: content,
        sentTime: sentTime,
      };

      setMessage("");
      console.log(socket.current);
      socket.current.send(JSON.stringify(chatMsg));

      return chatMsg;
    } else {
      console.log("websocket 연결이 닫혀있습니다!");
    }
  };
  return (
    <div>
      <MainHeader />
      <BackGround>
        <NavigationBar name="chatting" />
        <ChattingContainer>
          <ChatChat>
            {chatList?.map((chat) => (
              <div style={{ display: "flex", flexDirection: "column" }}>
                {chat?.type === "ENTER" ? (
                  <ChatOne style={{ alignSelf: "center" }}>
                    <ChatMes style={{ backgroundColor: "darkgray" }}>
                      --------------- {chat.content} ---------------
                    </ChatMes>
                  </ChatOne>
                ) : chat?.sender?.id === senderinfo.id ? (
                  <ChatOne>
                    <div>{moment(chat.sentTime).format("M/D hh:mm")}</div>
                    <ChatMes>{chat.content}</ChatMes>
                  </ChatOne>
                ) : (
                  <ChatOne style={{ alignSelf: "flex-start" }}>
                    <ChatMes style={{ backgroundColor: "orange" }}>
                      {chat.content}
                    </ChatMes>
                    <div>{moment(chat.sentTime).format("M/D hh:mm")}</div>
                  </ChatOne>
                )}
              </div>
            ))}
          </ChatChat>

          <ChatWhite>
            <input
              style={{
                width: "60vw",
                fontSize: "15px",
                border: "0",
                outline: "none",
              }}
              type="text"
              placeholder="메세지를 입력하세요"
              onChange={onChange}
              value={message}
            ></input>
            <SendButton type="submit" onClick={onClicksendMessage}>
              전송
            </SendButton>
          </ChatWhite>
        </ChattingContainer>
      </BackGround>
    </div>
  );
}
