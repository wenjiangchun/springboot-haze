<!DOCTYPE html>
<html>
<head>
    <title>报文数据</title>
    <#assign ctx=ctx.contextPath/>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <meta name="keywords" content="智能车联网大数据管理平台">
    <meta name="description" content="智能车联网大数据管理平台">
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx}/res/vsail/css/bootstrap.min.css?v=3.3.5" rel="stylesheet">
    <link href="${ctx}/res/vsail/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx}/res/vsail/css/animate.min.css" rel="stylesheet">
    <link href="${ctx}/res/vsail/css/style.min.css?v=4.0.0" rel="stylesheet">
    <script src="${ctx}/res/vsail/js/jquery.min.js?v=2.1.4" type="text/javascript"></script>
    <script src="${ctx}/res/vsail/js/bootstrap.min.js?v=3.3.5" type="text/javascript"></script>
    <script src="${ctx}/res/vsail/js/plugins/metisMenu/jquery.metisMenu.js" type="text/javascript"></script>
    <#--<script src="${ctx}/res/vsail/js/plugins/layer/layer.min.js" type="text/javascript"></script>-->
    <script src="${ctx}/res/layer/layer.js"></script>
    <script src="${ctx}/res/knockout/knockout-3.5.0.js" type="text/javascript"></script>
    <script src="${ctx}/res/vsail/js/plugins/slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
    <script src="${ctx}/res/underscore/underscore-min.js" type="text/javascript"></script>

    <#--<script src="${ctx}/res/adminLTE/bower_components/select2/dist/js/select2.full.min.js" type="text/javascript"></script>-->
    <script type="text/javascript">

        /*layer.config({
            skin: 'demo-class'
        });*/
        let myModel = {};
        function showMyModel(url, title, width, height, callBack) {
            myModel.id = layer.open({
                type: 2,
                title: title,
                shadeClose: true,
                shade: 0.8,
                area: [width, height],
                content: url
            });
            myModel.callBack = callBack;
        }

        function hideMyModal() {
            if (myModel.callBack != null) {
                myModel.callBack.apply(this, arguments);
            }
            layer.close(myModel.id);
        }
    </script>
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
                            <h5><img src="${ctx}/res/vsail/img/title.png">车辆报文数据(最新500条)</h5>
                        </div>
                        <div class="ibox-content">
                            <table class="table table-bordered table-striped">
                                <thead>
                                <tr>
                                    <th sName="id">时间</th>
                                    <th sName="vin">转换16进制报文</th>
                                    <#--<th sName="operate" columnRender="formatOperator">操作</th>-->
                                </tr>
                                </thead>
                                <tbody>
                                 <#list dts as dt>
                                     <tr>
                                         <td style="color:black; width: 150px">${dt[0]!}</td>
                                         <td style="color:black; text-align: left">${dt[1]}</td>
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

</body>
</html>
