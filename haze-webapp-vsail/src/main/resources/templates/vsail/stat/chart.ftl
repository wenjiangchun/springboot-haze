<!DOCTYPE html>
<html>
<head>
    <title>上线下线信息</title>
    <#include "../../common/v-head.ftl"/>
    <#include "../../common/ztree.ftl"/>
    <link rel="stylesheet" type="text/css" media="all" href="${ctx}/res/treeSelect/treeSelect.css"/>
    <style>
        .wrapper-content {
            padding: 20px;
            overflow: auto;
        }
        #testbtn {
            color:white;
            font-size: 14px;
        }
    </style>
    <script src="${ctx}/res/treeSelect/treeSelect.js"></script>
</head>
<body >
<div id="wrapper">
    <div class="row J_mainContent" id="content-main">
        <div class="wrapper wrapper-content animated fadeInRight wrapper-background">
            <div class="row">
                <div class="col-sm-12">
                    <div class="ibox ibox-background float-e-margins">
                        <div class="ibox-title">
                            <h5><img src="${ctx}/res/vsail/img/title.png">统计分析</h5>
                        </div>
                        <div class="ibox-content">
                            <div class="row">
                                <div class="col-sm-2 m-b-xs">
                                    <div class="form-group">
                                        <label class="control-label">所属机构</label>
                                        <div class="control-div" style="width: 220px">
                                            <#--<select class="form-control m-b" name="rootGroupId" data-bind="value: rootGroupId">
                                                <#list groupList as group >
                                                    <option value="${group.id}">${group.fullName}</option>
                                                </#list>
                                            </select>-->
                                            <#--<input class="form-control m-b" value="" id="groupBtn">-->
                                            <div class="treeSelect m-b"></div>
                                            <input type="hidden" class="form-control" name="" data-bind="value: groupId">
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-2 m-b-xs">
                                    <div class="form-group">
                                        <label class="control-label">开始时间</label>
                                        <div class="control-div" style="width: 220px">
                                            <input type="date" placeholder="开始日期" class="form-control" name="" data-bind="value: startDay">
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-2 m-b-xs">
                                    <div class="form-group">
                                        <label class="control-label">结束时间</label>
                                        <div class="control-div" style="width: 220px">
                                            <input type="date" placeholder="结束日期" class="form-control" name="" data-bind="value: endDay">
                                        </div>
                                    </div>
                                </div>
                                <div class="form-button">
                                    <button type="button" class="btn btn-w-m btn-info" data-bind="click: query"><i class="fa fa-bar-chart-o"></i> 统计</button>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6" id="main" style="height:400px;">
                                </div>
                                <div class="col-md-6" id="main1" style="height:400px;">
                                </div>
                            </div>
                            <br>
                            <div class="row">
                                <div class="col-md-6" id="main2" style="height:400px;">
                                </div>
                                <div class="col-md-6" id="main3" style="height:400px;">
                                </div>
                            </div>
                            <br>
                            <div class="row">
                                <div class="col-md-12" id="main4" style="height:400px;">
                                </div>
                            </div>
                            <br>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--右侧部分结束-->
</div>
<script type="text/javascript" src="${ctx}/res/echarts/echarts.min.js"></script>
<script type="text/javascript" src="${ctx}/res/echarts/chalk.js"></script>

<script type="text/javascript">
    let viewModel = {
        rootGroupId: ko.observable(''),
        groupId: ko.observable(''),
        startDay: ko.observable('${startDay}'),
        endDay:ko.observable('${endDay}'),
        query: function () {
            $.post('${ctx}/v/stat/getStatCount',{groupId: this.groupId(), startDay: this.startDay(), endDay: this.endDay()}, function(data){
                //开始绘制图形
                if (data != null) {
                    const modelFireCount =  data.modelFireCount;
                   //生成模型火警图
                    createPieChart('main', modelFireCount, '不同车型火警统计');
                    const groupFireCount = data.groupFireCount;
                   //生成线路火警图
                    createBarChart('main1', groupFireCount, '不同线路火警统计');

                    const modelBdCount =  data.modelbdCount;
                    //生成模型火警图
                    createPieChart('main2', modelBdCount, '不同车型故障统计');
                    const groupBbdCount = data.groupbdCount;
                    //生成线路火警图
                    createBarChart('main3', groupBbdCount, '不同线路故障统计');

                    //生成火警折线时间图
                    createLineChart('main4', data.statDays);
                }
            });
        }
    };

    $(function() {
        ko.applyBindings(viewModel);
        console.log(viewModel.rootGroupId());
        //viewModel.query();
        $.post('${ctx}/v/stat/getUserGroups', function(data) {
            $(".treeSelect").treeSelect({
                data:data,
                inputId:"testbtn",
                zTreeOnClick:function (event, treeId, treeNode) {
                   $('#testbtn').val(treeNode.name);
                   viewModel.groupId(treeNode.id);
                }
            });
        });
    });

    function createPieChart(divId, data, title) {
        let modelData = [];
        let modelNameData = [];
        _.each(data, function(item) {
            const name = item[0];
            const ct = item[4];
            let d = _.find(modelData, function(dt){
                return dt.name === name;
            });
            if (d != null) {
                d.value = d.value + ct;
            } else {
                modelData.push({'name':name, 'value': ct});
                modelNameData.push(name);
            }
        });
        const option = {
            title : {
                text: title,
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            toolbox: {
                feature: {
                    dataZoom: {
                        yAxisIndex: 'none'
                    },
                    restore: {},
                    saveAsImage: {}
                }
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: modelNameData
            },
            series : [
                {
                    name: '车辆类型',
                    type: 'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    data:modelData,
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        let myChart = echarts.init(document.getElementById(divId), 'chalk');
        myChart.setOption(option);
    }

    function createBarChart(divId, data, title) {
        let modelData = [];
        let modelNameData = [];
        let valueData = [];
        _.each(data, function(item) {
            const name = item[0];
            const ct = item[4];
            let d = _.find(modelData, function(dt){
                return dt.name === name;
            });
            if (d != null) {
                d.value = d.value + ct;
            } else {
                modelData.push({'name':name, 'value': ct});
                modelNameData.push(name);
            }
        });

        _.each(modelData, function(item) {
            valueData.push(item.value);
        });

        const option = {
            title : {
                text: title,
                x:'center'
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: modelNameData
            },
            toolbox: {
                feature: {
                    dataZoom: {
                        yAxisIndex: 'none'
                    },
                    restore: {},
                    saveAsImage: {}
                }
            },
            xAxis: {
                type: 'category',
                data: modelNameData
            },
            yAxis: {
                type: 'value'
            },
            series: [{
                data: valueData,
                type: 'bar'
            }]
        };

        let myChart = echarts.init(document.getElementById(divId), 'chalk');
        myChart.setOption(option);
    }

    function createLineChart(divId, dts) {
        //首先生成时间数据
        //获取开始日期
        let date = [];
        let data = [];
        _.each(dts, function(item) {
            date.push(item[0]);
            data.push(item[1]);
        });

        const option = {
            tooltip: {
                trigger: 'axis',
                position: function (pt) {
                    return [pt[0], '10%'];
                }
            },
            title: {
                left: 'center',
                text: '每日火警统计',
            },
            toolbox: {
                feature: {
                    dataZoom: {
                        yAxisIndex: 'none'
                    },
                    restore: {},
                    saveAsImage: {}
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: date
            },
            yAxis: {
                type: 'value',
                boundaryGap: [0, '100%']
            },
            dataZoom: [{
                type: 'inside'
            }, {
                handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
                handleSize: '80%',
                handleStyle: {
                    color: '#fff',
                    shadowBlur: 3,
                    shadowColor: 'rgba(0, 0, 0, 0.6)',
                    shadowOffsetX: 2,
                    shadowOffsetY: 2
                }
            }],
            series: [
                {
                    name:'火警数量',
                    type:'line',
                    smooth:true,
                    symbol: 'none',
                    sampling: 'average',
                    itemStyle: {
                        color: 'rgb(255, 70, 131)'
                    },
                    areaStyle: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: 'rgb(255, 158, 68)'
                        }, {
                            offset: 1,
                            color: 'rgb(255, 70, 131)'
                        }])
                    },
                    data: data
                }
            ]
        };
        let myChart = echarts.init(document.getElementById(divId), 'chalk');
        myChart.setOption(option);
    }
</script>
</body>
</html>
