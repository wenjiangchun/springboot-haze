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
                            <a href="#" class="offline-number" id="number1"><span>1</span></a>
                            <a href="#" class="offline-number" id="number2"><span>2</span></a>
                            <a href="#" class="offline-number" id="number3"><span>3</span></a>
                            <a href="#" class="offline-number" id="number4"><span>4</span></a>
                        </div>
                    </div>
                    <div class="m-t m-t1">
                        <div class="img-number img-number1">
                            <img src="${ctx}/res/vsail/img/car02.png"/>
                            <a href="#" class="offline-number" id="number5"><span>5</span></a>
                            <a href="#" class="offline-number" id="number6"><span>6</span></a>
                        </div>
                    </div>
                </div>
            </div>

            <div class="ibox ibox-bk float-e-margins">

                <div class="ibox-content">
                    <span class="tips-title">车辆信息</span>
                    <form class="form-horizontal m-t" id="commentForm" novalidate="novalidate">
                        <div class="form-group">
                            <label class="col-sm-5 control-label">VIN</label>
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
                                <span data-bind="text: drivingNum"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-5 control-label">线路</label>
                            <div class="col-sm-7 control-text">
                                <span data-bind="text: lineGroupName"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-5 control-label">场地名称</label>
                            <div class="col-sm-7 control-text">
                                <span data-bind="text: siteGroupName"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-5 control-label">场地地址</label>
                            <div class="col-sm-7 control-text">
                                <span data-bind="text: address"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-5 control-label">联系人</label>
                            <div class="col-sm-7 control-text">
                                <span data-bind="text: linker"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-5 control-label">电话</label>
                            <div class="col-sm-7 control-text">
                                <span data-bind="text: linkerMobile"></span>
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
                    <div class="ddd" style="height: 670px;overflow: auto;-ms-overflow-style: scrollbar;">
                        <div class="echarts-text">
                            <div class="echarts-title"><span>1号电池</span><span class="state state-zt2"><em></em>正常</span></div>
                            <div class="echarts " id="echarts-bar-chart1" style="height: 277px;width: 100%;"></div>
                        </div>
                        <div class="echarts-text" name="1F">
                            <div class="echarts-title"><span>2号电池</span><span class="state state-zt3"><em></em>一氧化碳浓度超标</span></div>
                            <div class="echarts " id="echarts-bar-chart2" style="height: 277px;"></div>
                        </div>
                        <div class="echarts-text" name="1F">
                            <div class="echarts-title"><span>3号电池</span><span class="state state-zt3"><em></em>一氧化碳浓度超标</span></div>
                            <div class="echarts " id="echarts-bar-chart3" style="height: 277px;"></div>
                        </div>
                        <div class="echarts-text" name="1F">
                            <div class="echarts-title"><span>4号电池</span><span class="state state-zt3"><em></em>一氧化碳浓度超标</span></div>
                            <div class="echarts " id="echarts-bar-chart4" style="height: 277px;"></div>
                        </div>
                        <div class="echarts-text" name="1F">
                            <div class="echarts-title"><span>5号电池</span><span class="state state-zt3"><em></em>一氧化碳浓度超标</span></div>
                            <div class="echarts " id="echarts-bar-chart5" style="height: 277px;"></div>
                        </div>
                        <div class="echarts-text" name="1F">
                            <div class="echarts-title"><span>6号电池</span><span class="state state-zt3"><em></em>一氧化碳浓度超标</span></div>
                            <div class="echarts " id="echarts-bar-chart6" style="height: 277px;"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${ctx}/res/vsail/js/jquery.slimscroll.js"></script>
<script src="https://cdn.bootcss.com/echarts/4.1.0.rc2/echarts.min.js"></script>
<#--<script src="${ctx}/res/vsail/js/demo/echarts-sensor.js"></script>-->
<script>
    let busViewModel = {
        vin: ko.observable(''),
        busNum: ko.observable(''),
        drivingNum: ko.observable(''),
        lineGroupName: ko.observable(''),
        siteGroupName: ko.observable(''),
        address: ko.observable(''),
        linker: ko.observable(''),
        linkerMobile: ko.observable(''),
        rootGroupName: ko.observable(''),
        updateBusData: function(data) {
            this.vin(data.vin);
            this.busNum(data.busNum);
            this.drivingNum(data.drivingNum);
            this.lineGroupName(data.lineGroupName);
            this.siteGroupName(data.siteGroupName);
            this.address(data.address);
            this.linker(data.linker);
            this.linkerMobile(data.linkerMobile);
            this.rootGroupName(data.rootGroupName);
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
            console.log(busData)
            getSensor();
        },5000);
        $('.ddd').slimScroll({
            height: '700px'
        });
        //加载传感器数据
        getSensor()
    });

    function getSensor() {
        const url = '${ctx}/v/stat/getSensor/${vin}';
        $.post(url, function(dts) {
            //解析数据 目前默认传感器为6个
            const sensorSize = 6;
            //处理时间轴
            let linX = [];
            _.each(dts, function(element, index, list) {
               const dataTime = element[1];
               linX.push(dataTime);
            });
            let startIndex = 5;
            let sensorDataLen = 5;
            for (let i = 1; i <= sensorSize; i++) {
                //循环判断是否有第i个传感器信息
                //一氧化碳浓度
                let value_co = [];
                //传感器温度
                let value_temp = [];
                //最后传感器数据
                let lastFireData = '';
                let lastErrorData = '';
                _.each(dts, function(element, index, list) {
                    let co = 0;
                    let temp = 0;
                    //从第几个开始循环
                    for (let j = startIndex; j < element.length;) {
                        const sn = element[j];
                        if (sn != null && i === parseInt(sn)) {
                            //继续往下取
                            temp = parseFloat(element[j + 1]);
                            co = parseFloat(element[j + 2]);
                            //lastFireData = element[j + 3];
                            //lastErrorData = element[j + 4];
                            break;
                        } else {
                            j = j + sensorDataLen;
                        }
                    }
                    value_co.push(co);
                    value_temp.push(temp);
                });
                //判断最后一条数据的传感器状态 0为不在线 1为正常 2为故障 3为火警
                let lastState = 0;
                const lastData = dts[dts.length -1];
                for (let j = startIndex; j < lastData.length;) {
                    const sn = lastData[j];
                    if (sn != null && i === parseInt(sn)) {
                        //继续往下取
                        lastFireData = lastData[j + 3];
                        lastErrorData = lastData[j + 4];
                        //判断状态
                        if (lastFireData.indexOf('1') !== -1) {
                            lastState = 3;
                            break;
                        }
                        if (lastErrorData.indexOf('1') !== -1) {
                            lastState = 2;
                            break;
                        }
                        lastState = 1;
                        break;
                    } else {
                        j = j + sensorDataLen;
                    }
                }
                //设置状态
                switch (lastState) {
                    case 1:
                        $('#number' + i).removeClass().addClass('online-number');
                        break;
                    case 2:
                        $('#number' + i).removeClass().addClass('error-number');
                        break;
                    case 3:
                        $('#number' + i).removeClass().addClass('fire-number');
                        break;
                    default:
                        $('#number' + i).removeClass().addClass('offline-number');
                }
                let charts = {
                    unit: '户数',
                    names: ['co浓度(ppm)', '环境温度(℃)'],
                    lineX: linX,
                    value: [
                        value_co,
                        value_temp
                    ]
                };
                let color = ['rgba(8,125,204', 'rgba(140,154,241'];
                let lineY = [];
                for(let i = 0; i < charts.names.length; i++) {
                    let x = i;
                    if(x > color.length - 1) {
                        x = color.length - 1
                    }
                    let data = {
                        name: charts.names[i],
                        type: 'line',
                        color: color[x] + ')',
                        smooth: true,
                        areaStyle: {
                            normal: {
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                    offset: 0,
                                    yAxisIndex: 0,
                                    color: color[x] + ', 0.9)'
                                }, {
                                    offset: 0.8,
                                    yAxisIndex: 1,
                                    color: color[x] + ', 0.9)'
                                }], false),
                                shadowColor: 'rgba(0, 0, 0, 0.1)',
                                shadowBlur: 10
                            }
                        },
                        symbol: 'circle',
                        symbolSize: 5,
                        data: charts.value[i]
                    };
                    lineY.push(data)
                }
                let myChart = echarts.init(document.getElementById('echarts-bar-chart' + i));
                var option = {
                    backgroundColor: '#02396b',
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: charts.names,
                        textStyle: {
                            fontSize: 12,
                            color: '#fefefe',

                        },
                        top: '15',
                        right: '10'
                    },
                    grid: {
                        top: '25%',
                        left: '2%',
                        right: '2%',
                        bottom: '2%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: charts.lineX,
                        axisLabel: {
                            textStyle: {
                                color: '#c6effe'
                            },
                            formatter: function(params) {
                                return params.split(' ')[0]
                            }
                        },

                    },
                    yAxis: [{
                        type: 'value',
                        min: 0,
                        max: 200,
                        interval: 20,
                        splitLine: {
                            lineStyle: {
                                show: false,
                                type: 'dashed',
                                color: "#364b6d"
                            },

                        },
                        axisLine: {
                            lineStyle: {
                                color: '#c6effe',
                            }
                        },

                    },
                        {
                            type: 'value',
                            min: 0,
                            max: 85,
                            interval: 10,
                            axisLabel: {
                                formatter: '{value}'
                            },
                            splitLine: {
                                show: false,
                                lineStyle: {
                                    show: false,
                                    type: 'dashed'
                                },
                            },
                            axisLine: {
                                lineStyle: {
                                    color: '#c6effe',
                                }
                            },
                        }
                    ],
                    series: lineY,
                };
                myChart.setOption(option);
                myChart.resize();
            }
            window.onresize = function() {
                //myChart1.resize();
                //myChart2.resize(); //若有多个图表变动，可多写
            }
        });
    }
</script>
</body>
</html>
