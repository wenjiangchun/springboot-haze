<!DOCTYPE html>
<html>
<head>
    <title>测试报文</title>
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
                            <h5><img src="${ctx}/res/vsail/img/title.png">测试报文(最新500条)</h5>
                        </div>
                        <div class="ibox-content">
                            <table class="table table-bordered table-striped">
                                <thead>
                                <tr>
                                    <th sName="id">时间</th>
                                    <th sName="vin">转换16进制报文</th>
                                    <th sName="vin">原始报文</th>
                                    <#--<th sName="operate" columnRender="formatOperator">操作</th>-->
                                </tr>
                                </thead>
                                <tbody>
                                 <#list dts as dt>
                                     <tr>
                                         <td style="color:black; width: 150px">${dt[0]!}</td>
                                         <td style="color:black; text-align: left">${dt[1]}</td>
                                         <td style="color:black; text-align: left">${dt[2]}</td>
                                     </tr>
                                 </#list>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--右侧部分结束-->
<script type="text/javascript" src="${ctx}/res/echarts/echarts.min.js"></script>
<script type="text/javascript" src="${ctx}/res/echarts/dark.js"></script>

<script type="text/javascript">

</script>
</body>
</html>
