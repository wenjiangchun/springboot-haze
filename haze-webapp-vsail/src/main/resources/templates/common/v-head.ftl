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
<link rel="stylesheet" href="${ctx}/res/vsail/css/model.css" />
<style>
    body .demo-class .layui-layer-title{background:#001e30 !important; color:#4abafa; border: none; font-size: 16px}
    body .demo-class .layui-layer-btn{border-top:1px solid #E9E7E7}
    body .demo-class .layui-layer-btn a{background:#fff;}
    body .demo-class .layui-layer-btn .layui-layer-btn1{background:#fff;}
</style>
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