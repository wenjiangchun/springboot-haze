<!DOCTYPE html>
<html>
<head>
    <#include "common/head.ftl"/>
    <style type="text/css">
        #allmap {
            width: 100%;
            height: 900px;
        }

        p {
            margin-left: 5px;
            font-size: 14px;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <#include "common/nav.ftl"/>
    <div class="content-wrapper">
        <div id="allmap"></div>
    </div>
    <#include "common/left.ftl"/>
</div>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=3.0&ak=yumm7VbyBmfk8j4vQEM2a6YkQuwraE4a"></script>
<script type="text/javascript"
        src="http://api.map.baidu.com/library/AreaRestriction/1.2/src/AreaRestriction_min.js"></script>
<script type="text/javascript"
        src="http://api.map.baidu.com/library/TextIconOverlay/1.2/src/TextIconOverlay_min.js"></script>
<script type="text/javascript"
        src="http://api.map.baidu.com/library/MarkerClusterer/1.2/src/MarkerClusterer_min.js"></script>
<script>
    initMenu('home_menu');
    let map = new BMap.Map("allmap");
    map.centerAndZoom(new BMap.Point(116.404, 39.915), 13);
    map.enableScrollWheelZoom();
    map.addControl(new BMap.NavigationControl());
    map.setMapStyleV2({
        styleId: '66af0100ed7010cd5b1084125cf20157'
    });
    let markers = [];
    let markerClusterer = new BMapLib.MarkerClusterer(map, {markers: markers});
    let websocket = null;
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
            addMarker(event.data);
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

    /**
     * 添加地图坐标点
     * @param data
     */
    function addMarker(data) {
        let point = data.split('|');
        let id = point[0];
        let x = point[1];
        let y = point[2];
        let m = point[3];
        let name = point[4];
        let marker = getMarker(id);
        if (marker != null) {
            //TODO 后续添加判断 如果当前时间大于新传递进来的数据时间则不处理
            markerClusterer.removeMarker(marker);
        }
        let pt = new BMap.Point(x, y);
        let icon = new BMap.Icon("${ctx}/resources/fx2.png", new BMap.Size(48, 48));
        marker = new BMap.Marker(pt);
        marker.setIcon(icon);
        marker.setRotation(m);
        marker.id = id;
        let label = new BMap.Label(name);
        label.setOffset(new BMap.Size(20, -20));
        label.setStyle({color: "white", fontSize: "14px", fontWeight: "bold"});
        marker.setLabel(label);
        //map.addOverlay(marker1);
        markerClusterer.addMarker(marker);
    }

    function getMarker(id) {
        let markers = markerClusterer.getMarkers();
        for (let i = 0; i < markers; i++) {
            if (markers[i].id === id) {
                return markers[i];
            }
        }
        return null;
    }
</script>
</body>
</html>
