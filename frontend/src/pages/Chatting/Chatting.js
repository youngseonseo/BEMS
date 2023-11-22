import NavigationBar from "../../components/NavBar/navbar";
import MainHeader from "../../components/MainHeader/header";
import { BackGround } from "./../Monitoring/energy/Floor_energy/FloorbillStyle";
import {
  ChatChat,
  ChattingContainer,
  ChatWhite,
  SendButton,
} from "./ChattingStyle";
import { useEffect, useRef, useState } from "react";
import stompjs from "stompjs";
import axios from "axios";

export default function ChattingPage() {
  const [intoChatdata, setIntoChatdata] = useState();
  const [message, setMessage] = useState("");
  const [chatList, setChatList] = useState([]);
  const [senderinfo, setSenderinfo] = useState([]);

  const onChange = (event) => setMessage(event.target.value);

  const handleSubmit = (event) => {
    event.preventDefault();
  };

  const onSubmitMessage = (event) => {
    if (message === "") {
      return;
    }
    event.preventDefault();
    console.log(chatMsg);
    console.log(event);
    // '전송' 버튼에 이벤트 리스너 부착 - 데이터 발행(pub) 요청
    stompClient.current.send(chatRoomURI, {}, JSON.stringify(chatMsg));
  };

  const accessToken = localStorage.getItem("accessToken");
  const intoChat = async () => {
    await axios
      .get("http://localhost:8080/chat/enter", {
        headers: { Authorization: `Bearer ${accessToken}` },
      })
      .then((res) => {
        console.log(res);
        setIntoChatdata(res.data);
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
  const stompEndPoint = "ws://localhost:8080/ws-stomp";
  const uuid = "148b7e89-1cf7-4620-b9e2-f6121b7db0d1";
  const chatRoomURI = "/pub/chatroom/" + uuid;
  const chatMsg = {
    chatRoom: {
      roomId: uuid,
    },
    type: "TALK",
    sender: {
      id: senderinfo?.id,
      username: senderinfo?.username,
    },
    content: message,
    sentTime: new Date(),
  };
  console.log(chatMsg);
  const stompClient = useRef(stompjs.over(new WebSocket(stompEndPoint)));
  useEffect(() => {
    getUserInfo();
    intoChat();

    stompClient.current.connect({}, () => {
      console.log("STOMP 연결 성공!");
      // topic 구독 요청
      stompClient.current.subscribe("/sub/chatroom/" + uuid, (message) => {
        // 해당 topic에 채팅 메시지(데이터)가 publish된 경우
        const jsonData = message.body;
        console.log("Received JSON Data: ", jsonData);
      });
      //Tell user name to the server 채팅방 입장 메세지

      stompClient.current.send(chatRoomURI, {}, JSON.stringify(chatMsg));
    });

    return () => {
      stompClient.current.disconnect();
    };
  }, []);
  return (
    <div>
      <MainHeader />
      <BackGround>
        <NavigationBar name="chatting" />
        <ChattingContainer>
          <ChatChat></ChatChat>
          <ChatWhite>
            <input
              style={{ width: "60vw" }}
              type="text"
              placeholder="메세지를 입력하세요"
              onChange={onChange}
              value={message}
            ></input>
            <SendButton onSubmit={onSubmitMessage}>전송</SendButton>
          </ChatWhite>
        </ChattingContainer>
      </BackGround>
    </div>
  );
}
