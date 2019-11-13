<!DOCTYPE html>
<html lang="zh">
<head>
    <title>智能车联网大数据管理平台</title>
    <#include "common/v-head.ftl"/>
    <script src="${ctx}/res/vsail/js/hplus.min.js?v=4.0.0" type="text/javascript"></script>
</head>
<body class="fixed-sidebar full-height-layout gray-bg" style="overflow:hidden">
<div id="wrapper">
    <!--左侧导航开始-->
    <#include "common/nav.ftl"/>
    <!--左侧导航结束-->
    <!--右侧部分开始-->
    <div id="page-wrapper" class="gray-bg dashbard-1">
        <div class="row">
            <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
                <span class="navbar-title">智能车联网大数据管理平台</span>
                <div class="nav-content">
                    <ul>
                        <li>
                            <a href="#" data-bind="click: function(){viewModel.invokeFrame(0) }"><span>注册车辆</span><b data-bind="text:totalData().length"></b></a>
                        </li>
                        <li>
                            <a href="#" data-bind="click: function(){viewModel.invokeFrame(1) }"><span>在线车辆</span><b data-bind="text:onlineData().length"></b></a>
                        </li>
                        <li>
                            <a href="#" data-bind="click: function(){viewModel.invokeFrame(2) }"><span>故障车辆</span><b data-bind="text:breakdownData().length"></b></a>
                        </li>
                        <li>
                            <a href="#" data-bind="click: function(){viewModel.invokeFrame(3) }"><span>火警车辆</span><b data-bind="text:fireData().length"></b></a>
                        </li>
                    </ul>
                </div>
                <ul class="nav navbar-top-links navbar-right">
                    <li class="user">
                        <span><img src="${ctx}/res/vsail/img/user-img.png"></span>
                        <h3>欢迎您！<@shiro.principal/> </h3>
                    </li>
                    <li class="dropdown">
                        <a href="#">
                            <i><img src="${ctx}/res/vsail/img/news.png"></i><span class="label label-primary">12</span>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>

        <div class="row J_mainContent" id="content-main">
            <iframe src="" width="100%" height="922.8px" id="content" frameborder="0" name="myframe"></iframe>
        </div>
    </div>
    <!--右侧部分结束-->
</div>
<script>

    let viewModel = {
		totalData: ko.observableArray([]),
		onlineData: ko.observableArray([]),
		fireData: ko.observableArray([]),
		breakdownData: ko.observableArray([]),
        updateBusData: function (data) {
            const eventCode = data.eventCode;
            if (eventCode === -1) { //删除信息
                if (existBusData(this.totalData(), data)) {
                    this.totalData(deleteBusData(this.totalData(), data));
                }
                if (existBusData(this.onlineData(), data)) {
                    this.onlineData(deleteBusData(this.onlineData(), data));
                }
                if (existBusData(this.fireData(), data)) {
                    this.fireData(deleteBusData(this.fireData(), data));
                }
                if (existBusData(this.breakdownData(), data)) {
                    this.breakdownData(deleteBusData(this.breakdownData(), data));
                }
            } else {
                //更新数据
                processData(data);
            }
        },
        invokeFrame: function(showCode) {
            if (myframe.initBusData !== undefined) {
                switch (showCode) {
                    case 1:
                        myframe.initBusData(this.onlineData(), showCode);
                        break;
                    case 2:
                        myframe.initBusData(this.breakdownData(), showCode);
                        break;
                    case 3:
                        myframe.initBusData(this.fireData(), showCode);
                        break;
                    default:
                        //首先进行排序
                        const sortedData = _.sortBy(this.totalData(), 'sortCode');
                        console.log(sortedData);
                        myframe.initBusData(sortedData, showCode);
                }

            }
        }
    };

    let websocket = null;
    $(function () {
        ko.applyBindings(viewModel);
        //首先加载所属车辆信息
        $.post('${ctx}/v/getBusData', 'json', function (data) {
            //viewModel.totalData(data);
            _.each(data, processData);
            //加载完成后建立websocket链接
            if ('WebSocket' in window) {
                websocket = new WebSocket("${config.value!}/websocket/<@shiro.principal/>");
                websocket.onopen = function () {
                    console.log("连接成功");
                    viewModel.invokeFrame(0);
                };

                websocket.onclose = function () {
                    console.log("退出连接");
                };

                websocket.onmessage = function (event) {
                    console.log("收到消息" + $.parseJSON(event.data));
                    //更新数值, 同时将收到的数据传送到地图界面
					viewModel.updateBusData($.parseJSON(event.data));
					//TODO 待完善
                    if (myframe.window.addMarker !== undefined) {
                        myframe.window.addMarker($.parseJSON(event.data))
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
    });

    function processData(busData) {
        viewModel.totalData(addOrUpdateBusData(viewModel.totalData(), busData));
        switch (busData.eventCode) {
            case 1: //上线
                viewModel.onlineData(addOrUpdateBusData(viewModel.onlineData(), busData));
                break;
            case 2: //下线
                viewModel.onlineData(deleteBusData(viewModel.onlineData(), busData));
                break;
            case 3: //实时运行
                const isFire = busData.isFire;
                const isBreakdown = busData.isBreakDown;
                if (isFire) {
                    viewModel.fireData(addOrUpdateBusData(viewModel.fireData(), busData));
                } else {
                    viewModel.fireData(deleteBusData(viewModel.fireData(), busData));
                }
                if (isBreakdown) {
                    viewModel.breakdownData(addOrUpdateBusData(viewModel.breakdownData(), busData));
                } else {
                    viewModel.breakdownData(deleteBusData(viewModel.breakdownData(), busData));
                }

                break;
            case 5: //更新车辆基本信息
                viewModel.onlineData(updateBusDataIfExist(viewModel.onlineData(), busData));
                viewModel.fireData(updateBusDataIfExist(viewModel.fireData(), busData));
                viewModel.breakdownData(updateBusDataIfExist(viewModel.breakdownData(), busData));
                break;
            default:
                break;
        }
        //console.log(viewModel.totalData())
    }

    function existBusData(busData, data) {
        const busId = data.id;
        for (let i = 0; i < busData.length; i++) {
			const dt = busData[i];
            if (dt.id === busId) {
                return true;
            }
        }
        return false;
    }

    function deleteBusData(busData, data) {
		const busId = data.id;
        for (let i = 0; i < busData.length; i++) {
			const dt = busData[i];
            if (dt.id === busId) {
                busData.splice(i, 1);
                break;
            }
        }
        return busData;
    }

    function addOrUpdateBusData(busData, data) {
		const busId = data.id;
        if (!existBusData(busData, data)) {
            busData.push(data);
        } else {
            for (let i = 0; i < busData.length; i++) {
				const dt = busData[i];
                if (dt.id === busId) {
                    busData[i] === data;
                    break;
                }
            }
        }
        return busData;
    }

    /**
     * 如果data存在于busData则更新，否则不做操作
     */
    function updateBusDataIfExist(busData, data) {
        const busId = data.id;
        for (let i = 0; i < busData.length; i++) {
            const dt = busData[i];
            if (dt.id === busId) {
                busData[i] === data;
                break;
            }
        }
        return busData;
    }

    /**
     * 获取所有注册车辆信息
     */
    function getBusData() {
        return viewModel.totalData();
    }
</script>
</body>

</html>