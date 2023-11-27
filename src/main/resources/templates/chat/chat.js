/* 서버와의 websocket 연결 요청 */
const url = "ws://localhost:8080/ws";
const socket = new WebSocket(url);

socket.onopen = () => {
    console.log("서버와의 websocket 연결이 성공적으로 이뤄졌습니다!");
};


/* 서버로 데이터 전송 */
if (socket.readyState == WebSocket.OPEN) {
    const roomId = "83fdc206-23c2-4665-9ad6-5f638d2a9f3d";
    const userId = 2;
    const username = "유저2";
    const content = "안녕하세요";
    const sentTime = new Date();

    const chatMsg = {
        "chatRoom": {
            "roomId": roomId
        },
        "type": "TALK",
        "sender": {
            "id": userId,
            "username": username
        },
        "content": content,
        "sentTime": sentTime
    };

    socket.send(JSON.stringify(chatMsg));
}
else {
    console.log('websocket 연결이 닫혀있습니다!');
}


/* 서버에서 보낸 데이터 수신 */
socket.onmessage = (event) => {
    console.log("chat message from server:", event.data);
};


/* 서버와의 websocket 연결 종료 */
socket.onclose = () => {
    console.log("서버와의 websocket 연결이 종료되었습니다!");
};


/* websocket 연결에 오류 발생 */
socket.onerror = (error) => {
    console.log("websocket 연결에 오류가 발생했습니다:", error);
};