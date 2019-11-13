<!DOCTYPE html>
<html lang="zh">
<head>
    <#include "../common/v-head.ftl"/>
    <link rel="stylesheet" href="${ctx}/res/vsail/css/tips.css" />
    <style type="text/css">
        .control-label{
            width: 170px;
            color: #7dcbf7;
        }
        .form-group{
            margin-bottom: 0px;
        }
    </style>
</head>
<body style="background: #001e30;min-width: 1280px !important;">
<div class="wrapper wrapper-content  fadeInRight" style="padding: 20px;overflow: auto !important;">
    <div class="row">
        <div class="col-sm-6">
            <div class="ibox ibox-bk float-e-margins">
                <div class="ibox-content">
                    <span class="tips-title">车辆电池分布图</span>
                    <div class="m-t m-t1">
                        <div class="img-number">
                            <img src="${ctx}/res/vsail/img/car01.png"/>
                            <a href="#1F" name="1F"><span>1</span></a>
                            <a href="#"><span>2</span></a>
                            <a href="#"><span>3</span></a>
                            <a href="#"><span>4</span></a>
                        </div>
                    </div>
                    <div class="m-t m-t1">
                        <div class="img-number img-number1">
                            <img src="${ctx}/res/vsail/img/car02.png"/>
                            <a href="#"><span>5</span></a>
                            <a href="#"><span>6</span></a>
                        </div>
                    </div>
                </div>
            </div>

            <div class="ibox ibox-bk float-e-margins">

                <div class="ibox-content">
                    <span class="tips-title">车辆信息</span>
                    <form class="form-horizontal m-t" id="commentForm" novalidate="novalidate">
                        <div class="form-group">
                            <label class="col-sm-5 control-label">VIN码</label>
                            <div class="col-sm-7 control-text">
                                <span data-bind="text: vin"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-5 control-label">公交自编号</label>
                            <div class="col-sm-7 control-text">
                                <span data-bind="text: busNum"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-5 control-label">客户名称</label>
                            <div class="col-sm-7 control-text">
                                <span data-bind="text: rootGroupName"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-5 control-label">车牌号</label>
                            <div class="col-sm-7 control-text">
                                <span data-bind="text: vin"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-5 control-label">线路</label>
                            <div class="col-sm-7 control-text">
                                <span data-bind="text: groupName"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-5 control-label">场地名称</label>
                            <div class="col-sm-7 control-text">
                                <span></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-5 control-label">联系人</label>
                            <div class="col-sm-7 control-text">
                                <span></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-5 control-label">电话</label>
                            <div class="col-sm-7 control-text">
                                <span></span>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-sm-6">
            <div class="ibox echarts-img float-e-margins">
                <div class="ibox-content">
                    <span class="tips-title">电池详细信息</span>
                    <div class="echarts " id="echarts-bar-chart" style="height: 300px;"></div>
                    <div class="echarts " id="echarts-bar-chart" style="height: 300px;"></div>
                    <div class="echarts " id="echarts-bar-chart" style="height: 300px;"></div>
                    <div class="echarts " id="echarts-bar-chart" style="height: 300px;"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    let busViewModel = {
        vin: ko.observable(''),
        busNum: ko.observable(''),
        registNum: ko.observable(''),
        motorNum: ko.observable(''),
        motorName: ko.observable(''),
        groupName: ko.observable(''),
        rootGroupName: ko.observable(''),
        updateBusData: function(data) {
            this.vin(data.vin);
            this.busNum(data.busNum);
            this.registNum(data.registNum);
            this.motorNum(data.motorNum);
            this.motorName(data.motorName);
            this.groupName(data.groupName);
            this.rootGroupName(data.rootGroupName);
            //同时更新图表数据 TODO
        }
    };
    $(function() {
        ko.applyBindings(busViewModel);
        const busId = "${busId}";
        const busData = parent.getBusData(busId);
        busViewModel.updateBusData(busData);
        setInterval(function() {
            const busData = parent.getBusData(busId);
            busViewModel.updateBusData(busData);
        },5000);
    });
</script>
</body>
</html>
