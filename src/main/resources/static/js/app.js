let stompClient = null;
let roomId = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    roomId = $("#room-id").val(); // 방 ID를 동적으로 설정
    if (!roomId) {
        alert("Room ID is required!");
        return;
    }

    stompClient = new StompJs.Client({
        brokerURL: `ws://trip-f.com/gs-guide-websocket`,
        onConnect: (frame) => {
            setConnected(true);
            console.log("Connected: " + frame);
            stompClient.subscribe(`/topic/chat/${roomId}`, (message) => {
                showGreeting(JSON.parse(message.body).content);
            });
        },
        onWebSocketError: (error) => {
            console.error("Error with WebSocket", error);
        },
        onStompError: (frame) => {
            console.error("Broker reported error: " + frame.headers["message"]);
            console.error("Additional details: " + frame.body);
        },
    });

    stompClient.activate();
}

function disconnect() {
    if (stompClient) {
        stompClient.deactivate();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    if (!stompClient || !roomId) {
        alert("You need to connect first!");
        return;
    }

    const name = $("#name").val();
    if (!name) {
        alert("Message content is required!");
        return;
    }

    stompClient.publish({
        destination: `/app/chat/sendMessage/${roomId}`,
        body: JSON.stringify({ content: name }), // 메시지 본문에 content 필드 추가
    });
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on("submit", (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendName());
});