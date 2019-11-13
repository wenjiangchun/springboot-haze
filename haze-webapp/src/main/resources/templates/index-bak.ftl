<!DOCTYPE html>
<html lang="zh">
<head>
    <title>车联网监控平台</title>
    <#include "common/head.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <#include "common/nav.ftl"/>
    <div class="content-wrapper">
        <iframe src="${ctx}/v/map" width="100%" height="922.8px"  id="content" frameborder="0" name="myframe"></iframe>
    </div>
    <!-- /.content-wrapper -->
    <#include "common/foot.ftl"/>
    <#include "common/left.ftl"/>
</div>
<script>
    initMenu('home_menu');
    let websocket = null;
    $(function() {
        $('.treeview-menu').find('a').click(function() {
            $this = $(this);
            const url = $this.attr('url');
            $('#content').attr('src', url);
            initMenu($this.attr('id'));
        });
        if ('WebSocket' in window) {
            websocket = new WebSocket("ws://192.168.31.103:8080/websocket/zhangsan");
            websocket.onopen = function () {
                console.log("连接成功");
            };

            websocket.onclose = function () {
                console.log("退出连接");
            };

            websocket.onmessage = function (event) {
                console.log("收到消息" + event.data);
                //更新数值, 同时将收到的数据传送到地图界面
                if (myframe.window.addMarker !== undefined) {
                    myframe.window.addMarker(event.data)
                }
            };

            websocket.onerror = function () {
                console.log("连接出错");
            };

            window.onbeforeunload = function () {
                websocket.close();
            };
        } else {
            console.warn('当前浏览器不支持websocket')
        }
    });
</script>
</body>
</html>
