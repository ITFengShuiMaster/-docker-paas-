var websocket = null;
//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
    websocket = new WebSocket("ws://localhost:8080/tianyu-paas/websocket");
}
else {
    alert('Not support websocket')
}

//连接发生错误的回调方法
websocket.onerror = function () {
    console.log("error");
};

//连接成功建立的回调方法
websocket.onopen = function (event) {
    console.log('open')
};

//接收到消息的回调方法   event.data
websocket.onmessage = function (event) {
    console.log(event)
    // if(event.data.type === 0) {
    //     examine.data.push(event.data.content);
    // }
    // if(event.data.type === 1) {
    //     change();
    // }
    // if(event.data.type === 2) {
    //     app.$message.info(event.data.content);
    //     app.notice.push(event.data.content);
    // }

};

//连接关闭的回调方法
websocket.onclose = function () {
    console.log('close')
};

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
    websocket.close();
};

//关闭连接
function closeWebSocket() {
    websocket.close();
}

//发送消息
function send() {
    var message = document.getElementById('text').value;
    websocket.send(message);
}