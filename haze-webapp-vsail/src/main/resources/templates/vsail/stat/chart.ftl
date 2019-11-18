<!DOCTYPE html>
<html>
<head>
    <title>上线下线信息</title>
    <#include "../../common/v-head.ftl"/>
    <style>
        .wrapper-content {
            padding: 20px;
            overflow: auto;
        }
    </style>
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
                                        <label class="control-label">运营公司</label>
                                        <div class="control-div" style="width: 220px">
                                            <select class="form-control m-b" name="rootGroupId" data-bind="value: rootGroupId">
                                                <#list groupList as group >
                                                    <option value="${group.id}">${group.fullName}</option>
                                                </#list>
                                            </select>
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
                            <div>

                            </div>
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

<script type="text/javascript">
    let viewModel = {
        rootGroupId: ko.observable(),
        startDay: ko.observable(),
        endDay:ko.observable(),
        query: function () {
            $.post('${ctx}/v/stat/getStatCount',{rootGroupId: this.rootGroupId(), startDay: this.startDay(), endDay: this.endDay()}, function(data){
                //开始绘制图形
            });
        }
    };

    $(function() {
        ko.applyBindings(viewModel);
        console.log(viewModel.rootGroupId());
        viewModel.query();
    });
</script>
</body>
</html>
