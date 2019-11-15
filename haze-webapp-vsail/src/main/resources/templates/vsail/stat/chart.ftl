<!DOCTYPE html>
<html>
<head>
    <title>上线下线信息</title>
    <#include "../../common/v-head.ftl"/>
    <#include "../../common/datatable.ftl"/>
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
                                            <select class="form-control m-b" name="rootGroupId">
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
                                            <input type="date" placeholder="开始日期" class="form-control" name="">
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-2 m-b-xs">
                                    <div class="form-group">
                                        <label class="control-label">结束时间</label>
                                        <div class="control-div" style="width: 220px">
                                            <input type="date" placeholder="结束日期" class="form-control" name="">
                                        </div>
                                    </div>
                                </div>
                                <div class="form-button">
                                    <button type="button" class="btn btn-w-m btn-info"><i class="fa fa-bar-chart-o"></i> 统计</button>
                                </div>
                            </div>
                            <div></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--右侧部分结束-->
</div>
<script>
    $(".nav-content li").click(function() {
        $(".sortable-list-box").show();
        $(".div-dw").show();
    })
    $(".btn.btn-sm.btn-white").click(function() {
        $(".sortable-list-box").show();
        $(".div-dw").show();
    })
    $(".div-dw").click(function() {
        $(this).hide();
        $(".sortable-list-box").hide();
    })
</script>
</body>
<script type="text/javascript">
    let viewModel;
    $(document).ready(function() {
        viewModel = {
            reset: function() {
                $(".datatable_query").val('');
            },
            query: function() {
                refreshTable();
            },
            show: function(id) {
                let url = "${ctx}/v/${name!}/edit/" + id;
                showMyModel(url,'编辑${cname!}', '900px', '50%');
            }
        };
        ko.applyBindings(viewModel);
        initDataTable();
    });


    function initDataTable() {
        const options = {
            divId : "contentTable",
            url : "${ctx}/v/stat/searchOnOff"
        };
        createTable(options);
    }

    function formatOperator(data) {
        let html = "";
        html += "<a href='javascript:void(0)' onclick='viewModel.delete(" + data.id + ")' title='查看'> <i class='fa fa-info-circle fa-lg'></i> </a>";
        return html;
    }

    function formatFlag(data) {
        return data.flag === 0 ? "<span class='label label-success'>上线</span>" : "<span class='label label-warning'>下线</span>"
    }
</script>
</html>
