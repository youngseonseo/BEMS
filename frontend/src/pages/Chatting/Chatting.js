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
import * as StompJs from "@stomp/stompjs";
import axios from "axios";

export default function ChattingPage() {
  const [intoChatdata, setIntoChatdata] = useState();
  const [message, setMessage] = useState(""); //메세지 유저 및 내용
  const [chatList, setChatList] = useState([]); //서버에서 받아온 내용
  const [senderinfo, setSenderinfo] = useState([]);
  const stompClient = useRef({});
  const onChange = (event) => setMessage(event.target.value);

  console.log(message);
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

  useEffect(() => {
    getUserInfo();
    intoChat();
    const stompEndPoint = "ws://localhost:8080/ws-stomp";
    const chatRoomURI = "/pub/chatroom/148b7e89-1cf7-4620-b9e2-f6121b7db0d1";
    const Stomp = StompJs.Stomp;
    const socket = new WebSocket(stompEndPoint);
    stompClient.current = Stomp.over(() => socket);
    const uuid = "148b7e89-1cf7-4620-b9e2-f6121b7db0d1";

    // 서버와의 연결이 성공한 경우(stomp 연결이 성공적으로 설정되었음)
    stompClient.current.connect({}, () => {
      console.log("STOMP 연결 성공!");

      // topic 구독 요청
      stompClient.current.subscribe("/sub/chatroom/" + uuid, (message) => {
        // 해당 topic에 채팅 메시지(데이터)가 publish된 경우
        const jsonData = message.body;
        console.log("Received JSON Data: ", jsonData);
      });

      //'전송' 버튼에 이벤트 리스너 부착 - 데이터 발행(pub) 요청
      document
        .getElementById("sendMessageButton")
        .addEventListener("click", (event) => {
          event.preventDefault();
          const roomId = "148b7e89-1cf7-4620-b9e2-f6121b7db0d1";
          const senderId = senderinfo.id;
          const senderUsername = senderinfo.username;
          const sentTime = new Date();
          const chatMsg = {
            chatRoom: {
              roomId: roomId,
            },
            type: "TALK",
            sender: {
              id: senderId,
              username: senderUsername,
            },
            content: message,
            sentTime: sentTime,
          };

          console.log(chatMsg);
          stompClient.current.send(chatRoomURI, {}, JSON.stringify(chatMsg));
          setMessage("");
        });
    });
    return () => stompClient.current.disconnect();
  }, []);

  return (
    <div>
      <MainHeader />
      <BackGround>
        <NavigationBar name="chatting" />
        <ChattingContainer>
          <ChatChat></ChatChat>
          <ChatWhite>
            <form>
              <input
                style={{ width: "60vw" }}
                type="text"
                placeholder="메세지를 입력하세요"
                onChange={onChange}
                value={message}
              ></input>
              <SendButton id="sendMessageButton">전송</SendButton>
            </form>
          </ChatWhite>
        </ChattingContainer>
      </BackGround>
    </div>
  );
}
