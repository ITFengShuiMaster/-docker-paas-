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
};

//接收到消息的回调方法   event.data
websocket.onmessage = function (event) {
    let res = JSON.parse(event.data);
    console.log(res);
    if (res.messageType === 'NOTICE') {
        vm.$message({
            message: '你收到了一条公告',
            type: 'success'
        });
        vm.noticerecord.push(res.data)
    }
    if (res.messageType === 'MESSAGE') {
        vm.$message({
            message: '你收到了一条消息',
            type: 'success'
        });
        vm.messagerecord.push(res.data)
    }
    if (res.messageType === 'BUILD_APPLICATION') {
        examine.data.push(res.data);
    }
    if (res.messageType === 'BUILD_APPLICATION_RESULT') {
        if (res.data === 1) {
            $('#content').load("pages/examine_finish.html", function (response, message, xhr) {
            });
        } else {

        }

    }

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