<!DOCTYPE html>
<html lang="zh">
<head>
    <title>智能车联网大数据管理平台</title>
    <#include "common/v-head.ftl"/>
    <link rel="stylesheet" href="${ctx}/res/toastr/toastr.css" />
    <link rel="stylesheet" href="${ctx}/res/vsail/css/layer.css" />
    <script src="${ctx}/res/vsail/js/hplus.min.js?v=4.0.0" type="text/javascript"></script>
    <script type="text/javascript" src="${ctx}/res/toastr/toastr1.js"></script>
    <style>
        .layui-layer-border {

        }
    </style>
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
                        <h3>欢迎您！<@shiro.principal/>  </h3>
                        <div class="user-content">
                            <ul>
                                <li><a href="javascript:void(0)" data-bind="click:updatePassword">修改密码</a></li>
                                <li><a href="${ctx}/logout">退出</a></li>
                            </ul>
                        </div>
                    </li>
                    <#--<li class="dropdown">
                        <a href="#">
                            <i><img src="${ctx}/res/vsail/img/news.png"></i><span class="label label-primary">12</span>
                        </a>
                    </li>-->
                </ul>
            </nav>
        </div>
        <div class="row J_mainContent" id="content-main">
            <iframe src="" width="100%" height="922.8px" id="content" frameborder="0" name="myframe"></iframe>
        </div>
    </div>
    <!--右侧部分结束-->
</div>
<div id="audio1">
</div>
<script>
    let viewModel = {
        eventCodeConstants:{
            ON: 1, //上线
            OFF: 2, //下线
            REAL: 3, //实时位置
            REGIST: 4, //注册
            UPDATE: 5, //更新
            DELETE: -1 //删除
        },
        fireVinData:ko.observableArray([]),
		totalData: ko.observableArray([]),
		onlineData: ko.observableArray([]),
		fireData: ko.observableArray([]),
		breakdownData: ko.observableArray([]),
        currentTab: ko.observable(-1),
        updateBusData: function (data) {
            const eventCode = data.eventCode;
            if (eventCode === this.eventCodeConstants.DELETE) { //删除信息
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
                this.currentTab(showCode);
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
                        myframe.initBusData(sortedData, showCode);
                }
            }
        },
        updateFrame: function(data) {
            if (myframe.initBusData !== undefined) {
                myframe.updateBusData(this.totalData());
            }
        },
        updatePassword: function() {
            layer.prompt({title: '请输入新密码', formType: 1}, function(val, index){
                $.post('${ctx}/system/user/updatePassword',{newPassword:val, id:<@shiro.principal property="userId" />}, function(data) {
                    if(data === "SUCCESS") {
                        layer.alert('密码修改成功');
                        layer.close(index);
                    }
                });
            });
        }
    };

    let websocket = null;
    let hasFire = 0;
    $(function () {
        ko.applyBindings(viewModel);
        //首先加载所属车辆信息
        $.post('${ctx}/v/getBusData', 'json', function (data) {
            //viewModel.totalData(data);
            _.each(data, processData);
            //加载完成后建立websocket链接
            if ('WebSocket' in window) {
                websocket = new WebSocket("${config.value!}/websocket/<@shiro.principal/>");
                //websocket = new WebSocket("ws://localhost:8080/websocket/<@shiro.principal/>");
                websocket.onopen = function () {
                    console.log("连接成功");
                    viewModel.invokeFrame(-1);
                };

                websocket.onclose = function () {
                    console.log("退出连接");
                };

                websocket.onmessage = function (event) {
                    console.log("收到消息" + event.data);
                    //更新数值, 同时将收到的数据传送到地图界面
                    //判断是否需要更新map页面
                    const busData = $.parseJSON(event.data);
					viewModel.updateBusData(busData);
                    viewModel.invokeFrame(viewModel.currentTab());
					//TODO 待完善
                    /*if (myframe.window.addMarker !== undefined) {
                        myframe.window.addMarker($.parseJSON(event.data))
                    }*/
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

        setInterval(function() {
            console.log(_.size(viewModel.fireVinData()));
            if (_.size(viewModel.fireVinData()) > 0) {
                let alarm = $('#audio1');
                alarm.html('<audio src="${ctx}/res/vsail/file/fireAlarm.wav" autoplay="autoplay"></audio>');
            } else {
                let alarm = $('#audio1');
                alarm.html('');
            }
        },5000)
    });

    function processData(busData) {
        viewModel.totalData(addOrUpdateBusData(viewModel.totalData(), busData));
        switch (busData.eventCode) {
            case viewModel.eventCodeConstants.ON: //上线
                viewModel.onlineData(addOrUpdateBusData(viewModel.onlineData(), busData));
                break;
            case viewModel.eventCodeConstants.OFF: //下线
                viewModel.onlineData(deleteBusData(viewModel.onlineData(), busData));
                break;
            case viewModel.eventCodeConstants.REAL: //实时运行
                viewModel.onlineData(addOrUpdateBusData(viewModel.onlineData(), busData));
                const isFire = busData.fire;
                const isBreakdown = busData.breakDown;
                if (isFire) {
                    viewModel.fireData(addOrUpdateBusData(viewModel.fireData(), busData));
                    showFireNotify(busData);
                } else {
                    viewModel.fireData(deleteBusData(viewModel.fireData(), busData));
                }
                if (isBreakdown) {
                    viewModel.breakdownData(addOrUpdateBusData(viewModel.breakdownData(), busData));
                } else {
                    viewModel.breakdownData(deleteBusData(viewModel.breakdownData(), busData));
                }

                break;
            case viewModel.eventCodeConstants.UPDATE: //更新车辆基本信息
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
        const busId = data.vin;
        for (let i = 0; i < busData.length; i++) {
			const dt = busData[i];
            if (dt.vin === busId) {
                return true;
            }
        }
        return false;
    }

    function deleteBusData(busData, data) {
		const busId = data.vin;
        for (let i = 0; i < busData.length; i++) {
			const dt = busData[i];
            if (dt.vin === busId) {
                busData.splice(i, 1);
                break;
            }
        }
        return busData;
    }

    function addOrUpdateBusData(busData, data) {
		const busId = data.vin;
        if (!existBusData(busData, data)) {
            busData.push(data);
        } else {
            for (let i = 0; i < busData.length; i++) {
				const dt = busData[i];
                if (dt.vin === busId) {
                    if (data.eventCode === viewModel.eventCodeConstants.UPDATE) {
                        data.eventCode = dt.eventCode;
                    }
                    busData[i] = data;
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
        const busId = data.vin;
        for (let i = 0; i < busData.length; i++) {
            const dt = busData[i];
            if (dt.vin === busId) {
                busData[i] = data;
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


    function showFireNotify(data) {
        //判断当前火警是否已存在弹窗中
        let fireVinData = viewModel.fireVinData();
        if (! _.contains(fireVinData, data.vin))  {
            fireVinData.push(data.vin);
            viewModel.fireVinData(fireVinData);
            toastr.options = {
                closeButton: true,
                positionClass: 'toast-bottom-right',
                timeOut: 0,
                extendedTimeOut: 0,
                hideEasing:'',
                onclick:function() {
                    viewModel.fireVinData(_.reject(fireVinData, function(vin){ return vin === data.vin; }));
                    if (myframe.initBusData !== undefined) {
                        myframe.showFire(data);
                    }
                },
                onCloseClick:function() {
                    viewModel.fireVinData(_.reject(fireVinData, function (vin) {
                        return vin === data.vin;
                    }));
                }
            };
            toastr.error('vin=.'+ data.vin, '监测到火警信息 ' + data.sendTime + '');
        }
    }

</script>
</body>
</html>