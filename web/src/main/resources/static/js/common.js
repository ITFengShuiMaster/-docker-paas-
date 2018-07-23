window.web = {};

web.baseUrl = 'http://localhost:8080/tianyu-paas/';

web.url = {
    login: web.baseUrl + 'user/login',

    // 获取组下面所有的app
    listGroupApps: web.baseUrl + 'app-groups/',
    startApp: web.baseUrl + 'apps/start/',
    stopApp: web.baseUrl + 'apps/stop/',
    restartApp: web.baseUrl + 'apps/restart-container/',
    deleteApp: web.baseUrl + 'apps/delete/',
    batchStartApp: web.baseUrl + 'apps/batch-start',
    batchStopApp: web.baseUrl + 'apps/batch-stop',
    batchRestartApp: web.baseUrl + 'apps/batch-restart',

    websocket: "ws://123.207.169.68:8080/web/websocket"
};

web.result = {
    success: 1,
    failure: -1
};

web.ajax = function (options, vm) {
    const loading = vm.$loading({
        lock: true,
        text: '加载数据中，请稍候',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
    });
    let ajaxDefaults = {
        // ContentType: 'application/x-www-form-urlencoded',
        /*json:charset=utf-8*/
        dataType: 'json',
        type: 'get',
        complete: function (XMLHttpRequest, textStatus) {
            // vm.$loading.close();
            loading.close();
        },
        error: function (xhr, status, error) {
            // 显示一个3秒的错误提示
            vm.$message.error('加载过程好像遇到了点意外');
        },
    };
    $.ajax($.extend(ajaxDefaults, options));
};

web.appStatusNames = ['已关闭', '运行中', '异常'];
web.appCreateMethodNames = ['自定义', '官方demo', '', 'github项目', 'docker镜像', '应用市场'];

