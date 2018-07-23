window.ms = {};

ms.baseUrl = 'http://localhost:8081/ms/';

ms.url = {
    login: ms.baseUrl + 'user/login',

    listCustomerServices: ms.baseUrl + 'customer-services/list',
    deleteCustomerService: ms.baseUrl + 'customer-services/',
    updateCustomerService: ms.baseUrl + 'customer-services/',
    addCustomerService: ms.baseUrl + 'customer-services/',

    websocket: "ws://123.207.169.68:8080/ms/websocket"
}
;

ms.result = {
    success: 1,
    failure: -1
};

ms.ajax = function (options, vm) {
    let ajaxDefaults = {
        ContentType: 'application/x-www-form-urlencoded',
        /*json:charset=utf-8*/
        dataType: 'json',
        complete: function (XMLHttpRequest, textStatus) {
            // vm.$Modal.remove();
            vm.$Spin.hide();
        },
        error: function (xhr, status, error) {
            // 显示一个3秒的错误提示
            vm.$Message.error({
                content: '请求发生错误：' + error,
                duration: 3,
                closable: true
            });
        },
    };
    vm.$Spin.show({
        render: (h) => {
            return h('div', [
                h('Icon', {
                    'class': 'demo-spin-icon-load',
                    props: {
                        type: 'load-c',
                        size: 30
                    }
                }),
                h('div', 'Loading')
            ])
        }
    });
    $.ajax($.extend(ajaxDefaults, options));
};