<!DOCTYPE html>
<html lang="zh">
<head>
    <#include "../common/v-head.ftl"/>
    <link rel="stylesheet" href="${ctx}/res/toastr/toastr.css" />
    <link rel="stylesheet" href="${ctx}/res/vsail/css/layer.css" />
    <style type="text/css">
        #allmap {
            width: 99%;
            height: 923px;
        }
        .infoBoxContent{font-size:12px;background: royalblue;}
    </style>
</head>
<body class="fixed-sidebar full-height-layout gray-bg" style="overflow:hidden">
<div id="wrapper">
    <div class="row J_mainContent" id="content-main">
        <div class="map">
            <div id="allmap"></div>
            <div class="input-serch">
                <div class="input-group">
                    <input type="text" class="input input-sm form-control" id="query" data-bind="value:keyWorld" placeholder="VIN码/公交自编号/车牌号"/>
                    <span class="input-group-btn">
                          <button type="button" class="btn btn-sm btn-white" data-bind="click:function(){busViewModel.query(1)}"><i><img src="${ctx}/res/vsail/img/souser.png"></i></button>
                    </span>
                </div>
                <div class="ibox list-news">
                    <div class="sortable-list-box" id="divFloatToolsView">
                        <div class="sortable-list-title">
                            <span>车辆编号</span>
                            <span>状态</span>
                            <span>报警情况</span>
                        </div>
                        <ul class="sortable-list connectList agile-list ui-sortable" data-bind="foreach:showBusData">
                            <li data-bind="attr: {'id':id,'vin':vin}, visible:show, class:className, click:function(){busViewModel.openLayer($data.id,$data.vin)}">
                                <span class="lable-name" data-bind="text: vin"></span>
                                <span class="lable-state" data-bind="text: state"></span>
                                <span class="lable-give" data-bind="text: desc"></span>
                            </li>
                        </ul>
                        <nav class="page" aria-label="Page navigation" style="text-align: center;">
                            <ul class="pagination" style="margin-top: 15px;" data-bind="foreach:totalPage">
                                <li data-bind="if: $data==1">
                                    <a href="#" aria-label="Previous" data-bind="click: function(){busViewModel.query(1)}">
                                        <span aria-hidden="true">&laquo;</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="#" data-bind="click: function(){busViewModel.query($data)}, text: $data"></a>
                                </li>
                                <li data-bind="if: $data==busViewModel.totalPage().length">
                                    <a href="#" aria-label="Next" data-bind="click: function(){busViewModel.query(busViewModel.totalPage().length)}">
                                        <span aria-hidden="true">&raquo;</span>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                    <div class="floatL div-dw">
                        <a style="display:block" id="aFloatTools_Show" class="btnOpen" title="查看" style="top:20px" href="javascript:void(0);"></a>
                        <a style="display:none" id="aFloatTools_Hide" class="btnCtn" title="关闭" style="top:20px" href="javascript:void(0);"></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="audio1">
</div>

<script type="text/template" id="tpl">
    <div class="infoBoxContent">
        <table class="table table-bordered">
            <tr>
                <th>VIN码</th>
                <td><%=vin%></td>
            </tr>
            <tr>
                <th>车牌号</th>
                <td><%=drivingNum%></td>
            </tr>
            <tr>
                <th>公交自编号</th>
                <td><%=busNum%></td>
            </tr>
            <tr>
                <th>场站</th>
                <td><%=siteGroupName%></td>
            </tr>
            <tr>
                <th>场站地址</th>
                <td><%=address%></td>
            </tr>
            <tr>
                <th>联系人</th>
                <td><%=linker%></td>
            </tr>
            <tr>
                <th>联系人电话</th>
                <td><%=linkerMobile%></td>
            </tr>
            <tr>
                <th>所属公司</th>
                <td><%=rootGroupName%></td>
            </tr>
            <tr>
                <th>所属线路</th>
                <td><%=lineGroupName%></td>
            </tr>
            <tr>
                <td colspan="2">
                    <button class="btn btn-xs btn-warning" onclick="busViewModel.openLayer(<%=id%>,<%=vin%>)">详情</button>
                </td>
            </tr>
        </table>
    </div>
</script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=3.0&ak=1vgKWyQpm6tkYp6X88Wi0w7DhtGM1bbH"></script>
<script type="text/javascript" src="http://api.map.baidu.com/library/AreaRestriction/1.2/src/AreaRestriction_min.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/library/TextIconOverlay/1.2/src/TextIconOverlay_min.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/library/MarkerClusterer/1.2/src/MarkerClusterer_min.js"></script>
<script type="text/javascript" src="${ctx}/res/toastr/toastr.js"></script>
<script type="text/javascript" src="${ctx}/res/map/InfoBox.js"></script>
<script type="text/javascript" src="${ctx}/res/vsail/js/jquery.slimscroll.js"></script>
<script>
    function BusMap(containerId, styleId) {
        let map = new BMap.Map(containerId);
        map.centerAndZoom(new BMap.Point(116.404, 39.915), 13);
        map.enableScrollWheelZoom();
        const top_right_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL});
        map.addControl(top_right_navigation);
        map.setMapStyleV2({
            styleId: styleId
        });
        this.map = map;
        this.markerCluster = new BMapLib.MarkerClusterer(map, {markers: []});
        const sContent = '<div class="infoBoxContent"><table class="table table-bordered"><tr><th>vin码</th><td></td></tr><tr><th>车牌号</th><td></td></tr><tr><th>行驶证</th><td></td></tr><tr><th>发动机编号</th><td></td></tr><tr><th>发动机型号</th><td></td></tr></table></div>';
        this.infoBox = null;
        //this.infoBox.setContent('aaasdfsdfsd');
        //this.infoBox.initialize(map);
        this.infoBoxTemplate = _.template($("#tpl").html());
    }

    BusMap.prototype.addMarker = function(data) {
        const name = data.vin;
        const id = data.id;
        let x = 116.404 + Math.random()/10, y = 39.915 + Math.random()/10;
        if (! (_.isNull(data.x) || _.isNull(data.y))) {
            x = data.x;
            y = data.y;
        }
        let marker = this.getMarker(id);
        if (marker != null) {
            //TODO 后续添加判断 如果当前时间大于新传递进来的数据时间则不处理
            this.markerCluster.removeMarker(marker);
        }
        let pt = new BMap.Point(x, y);
        let iconFile = "${ctx}/res/dot-1.png";
        if (data.breakDown) {
            iconFile = "${ctx}/res/dot-b.png";
        }
        if (data.fire) {
            iconFile = "${ctx}/res/dot-f.gif";
        }
        let icon = new BMap.Icon(iconFile, new BMap.Size(36, 36));
        marker = new BMap.Marker(pt);
        marker.setIcon(icon);
        marker.id = data.vin;
        marker.busId = id;
        marker.busData = data;
        let label = new BMap.Label(name);
        label.setOffset(new BMap.Size(20, -20));
        label.setStyle({color: "white", fontSize: "14px", fontWeight: "bold"});
        //marker.setLabel(label);
        //map.addOverlay(marker1);
        this.markerCluster.addMarker(marker);
        //添加窗口
        const $this = this;
        marker.addEventListener("click", function(){
            $this.selectMarker(marker.id);
        });
    };

    BusMap.prototype.getMarker = function(id) {
        let markers = this.markerCluster.getMarkers();
        for (let i = 0; i < markers.length; i++) {
            if (markers[i].id === id) {
                return markers[i];
            }
        }
        return null;
    };

    BusMap.prototype.clearMarkers = function() {
        this.markerCluster.clearMarkers();
    };

    BusMap.prototype.centerMarker = function(id) {
        const marker = this.getMarker(id);
        if (marker != null) {
            this.map.panTo(new BMap.Point(marker.getPosition().lng, marker.getPosition().lat));
        }
    };
    BusMap.prototype.selectMarker = function(id) {
        const marker = this.getMarker(id);
        if (marker != null) {
            const data = marker.busData;
            this.map.panTo(new BMap.Point(marker.getPosition().lng, marker.getPosition().lat));
            if (this.infoBox != null) {
                this.infoBox.close();
            }
            if (data.id == null) {
                data.id = -1;
            }
            this.infoBox = new BMapLib.InfoBox(this.map,this.infoBoxTemplate(data),{
                boxStyle:{width: "300px"
                },
                closeIconMargin: "5px 5px 0 0",
                closeIconUrl:'${ctx}/res/close5.png',
                align:INFOBOX_AT_TOP,
                offset: new BMap.Size(0, 0),
            enableAutoPan: false
            });
            this.infoBox.open(marker);
        }

    };

    let myMap = new BusMap("allmap", '94549fedffbeb65f9e3a99373f691b59');
    const pageSize = 20;
    /**
     * 当前车辆信息
     * busData:所有车辆信息，当有新的车辆数据进来时更新该数据
     * showCode: 0 已注册， 1已在线， 2 故障， 3 火灾 默认已注册(0)
     */
    let busViewModel = {
        busData:ko.observableArray([]),
        showBusData:ko.observableArray([]),
        totalPage:ko.observableArray([]),
        pageIndex:ko.observable(1),
        showCode:ko.observable(-1),
        keyWorld:ko.observable(''),
        validBusData: function(data) {
            switch (this.showCode()) {
                case 1:
                    return data.online;
                case 2:
                    return data.breakDown;
                case 3:
                    return data.fire;
                default:
                    return true;
            }
        },
        processBusData: function(dts, showCode, map) {
            if (map.infoBox != null) {
                map.infoBox.close();
            }
            map.clearMarkers();
            this.totalPage([]);
            this.showBusData([]);
            let showBusDts = [];
            _.each(dts, function(bus) {
                //根据showCode计算所属样式类 TODO
                bus.desc = '正常';
                bus.className = 'info-element';
                bus.sn = 3;
                if (bus.online) {
                    //bus.state =  '在线';
                    bus.className = 'success-element';
                    bus.sn = 2;
                }

                if (bus.breakDown) {
                    //bus.state =  '故障';
                    //TODO
                    const sensores = bus.sensores;
                    bus.desc = '';
                    for (let i = 0; i < sensores.length; i++) {
                        if (sensores[i].state === 2) {
                            bus.desc += " #" + sensores[i].sn;
                        }
                    }
                    bus.desc += " 传感器故障";
                    bus.className = 'warning-element';
                    bus.sn = 1;
                }
                if (bus.fire) {
                    //bus.state = '火警';
                    //TODO
                    //bus.desc = '火警喷发';
                    const sensores = bus.sensores;
                    bus.desc = '';
                    for (let i = 0; i < sensores.length; i++) {
                        if (sensores[i].state >= 3) {
                            bus.desc += " #" + sensores[i].sn;
                        }
                    }
                    bus.desc += " 传感器报警";
                    bus.className = 'danger-element';
                    bus.sn = 0;
                }
                const keyWorld = busViewModel.keyWorld();
                if (bus.vin.indexOf(keyWorld) === -1 && bus.drivingNum.indexOf(keyWorld) === -1 && bus.busNum.indexOf(keyWorld) === -1) {
                    bus.show = false;
                } else {
                    bus.show = true;
                    showBusDts.push(bus);
                    if (bus.state !== '离线') {
                        map.addMarker(bus);
                    }

                }
            });
            //对showBusDts进行排序
            showBusDts =  _.sortBy(showBusDts, 'sn');
            //计算共有几页
            const totalPage = showBusDts.length % pageSize === 0 ? showBusDts.length/pageSize : showBusDts.length/pageSize + 1;
            let temp = [];
            for (let i = 1; i <= totalPage; i++) {
                temp.push(i);
            }
            this.totalPage(temp);
            const start = (this.pageIndex() - 1) * pageSize;
            this.showBusData(_.filter(showBusDts, function(data, idx){ return idx >=start && idx < (start + pageSize); }));

        },
        query: function(pageSize) {
            this.pageIndex(pageSize);
            this.processBusData(this.busData(), this.showCode(), myMap);
            $('#divFloatToolsView').animate({
                width: 'show',
                opacity: 'show'
            }, 100, function() {
                $('#divFloatToolsView').show();
            });
            $('#aFloatTools_Show').hide();
            $('#aFloatTools_Hide').show();
        },
        openLayer: function(busId, vin) {
            hideMyModal();
            /*let url = "${ctx}/v/monitorCell/" + 8 + '/' + vin;
            showMyModel(url,'车辆实时信息【vin=' + vin + '】', '80%', '80%', function() {
            });
            return;*/
            //在地图中心显示该点坐标
            myMap.centerMarker(vin);
            if (busId != null && busId !== -1) {
                let url = "${ctx}/v/monitorCell/" + busId + '/' + vin;
                let $this = this;
                showMyModel(url,'车辆实时信息【vin=' + vin + '】', '80%', '95%', function() {
                });

            } else {
                alert('该车辆未录入系统，不能查看详情信息');
            }

        },
        clear: function(map) {
            //hideMyModal();
            map.clearMarkers();
        },
        showFireNotify: function(data) {
            toastr.options = {
                closeButton: true,
                positionClass: 'toast-bottom-right',
                onclick: null,
                timeOut: 50000
            };
            toastr.error('vin=收到火警信息.', '检测到火警信息')
        }
    };
    ko.applyBindings(busViewModel);
    $(function() {
        $(".nav-content li").click(function() {
            $('#divFloatToolsView').animate({
                width: 'show',
                opacity: 'show'
            }, 100, function() {
                $('#divFloatToolsView').show();
            });
            $('#aFloatTools_Show').hide();
            $('#aFloatTools_Hide').show();
        });
        $("#aFloatTools_Show").click(function() {
            $('#divFloatToolsView').animate({
                width: 'show',
                opacity: 'show'
            }, 100, function() {
                $('#divFloatToolsView').show();
            });
            $('#aFloatTools_Show').hide();
            $('#aFloatTools_Hide').show();
        });
        $("#aFloatTools_Hide").click(function() {
            $('#divFloatToolsView').animate({
                width: 'hide',
                opacity: 'hide'
            }, 100, function() {
                $('#divFloatToolsView').hide();
            });
            $('#aFloatTools_Show').show();
            $('#aFloatTools_Hide').hide();
        });

        if (busViewModel.busData().length === 0) {
            busViewModel.busData(parent.getBusData());
            initBusData(busViewModel.busData(), -1);
        }
        //$("#aFloatTools_Show").click();

        //TODO 测试
       /* let alarm = $('#audio1')
        for (let i = 0; i < 5; i++) {
            busViewModel.showFireNotify('ttt');
            alarm.html('<audio src="${ctx}/res/vsail/file/fireAlarm.wav" autoplay="autoplay"></audio>')
        }*/


    });


    function initBusData(dts, showCode) {
        //清空所有marker , 清空弹出窗 TODO
        busViewModel.clear(myMap);
        busViewModel.processBusData(dts, showCode, myMap);
        busViewModel.busData(dts);
        busViewModel.showCode(showCode);
        if (showCode !== -1) {
            $("#aFloatTools_Show").click();
        }
    }

    function getBusData(busId) {
        for (let i = 0; i < busViewModel.busData().length; i++) {
            if (busViewModel.busData()[i].id === busId) {
                return busViewModel.busData()[i];
            }
        }
        return null;
    }

    function showFire(data) {
        const marker = myMap.getMarker(data.vin);
        if (marker == null) {
            myMap.addMarker(data);
        }
        myMap.selectMarker(data.vin);
    }

</script>
</body>
</html>
