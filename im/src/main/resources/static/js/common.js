window.im = {};

im.baseUrl = 'http://localhost:8080/im/';

im.url = {
    login: im.baseUrl + 'users/login',

    websocket: "ws://123.207.169.68:8080/im/websocket"
};

im.result = {
    success: 1,
    failure: -1
};

im.ajax = function (options) {
    let ajaxDefaults = {
        // ContentType: 'application/x-www-form-urlencoded',
        /*json:charset=utf-8*/
        dataType: 'json',
        type: 'get',
        complete: function (XMLHttpRequest, textStatus) {
            // vm.$loading.close();
        },
        error: function (xhr, status, error) {
            // 显示一个3秒的错误提示
            alert('加载过程好像遇到了点意外');
        },
    };
    $.ajax($.extend(ajaxDefaults, options));
};