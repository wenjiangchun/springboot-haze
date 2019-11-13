<#assign ctx=ctx.contextPath/>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<!-- Bootstrap 3.3.7 -->
<link rel="stylesheet" href="${ctx}/res/adminLTE/bower_components/bootstrap/dist/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet" href="${ctx}/res/adminLTE/bower_components/font-awesome/css/font-awesome.min.css">
<!-- Ionicons -->
<link rel="stylesheet" href="${ctx}/res/adminLTE/bower_components/Ionicons/css/ionicons.min.css">
<!-- Theme style -->
<link rel="stylesheet" href="${ctx}/res/adminLTE/dist/css/AdminLTE.min.css">
<!-- AdminLTE Skins. Choose a skin from the css/skins
     folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet" href="${ctx}/res/adminLTE/dist/css/skins/_all-skins.min.css">

<!-- Date Picker -->
<link rel="stylesheet"
      href="${ctx}/res/adminLTE/bower_components/bootstrap-datepicker/dist/css/bootstrap-datepicker.min.css">
<!-- Daterange picker -->
<link rel="stylesheet" href="${ctx}/res/adminLTE/bower_components/bootstrap-daterangepicker/daterangepicker.css">
<link rel="stylesheet" href="${ctx}/res/adminLTE/bower_components/select2/dist/css/select2.min.css">
<link rel="stylesheet" href="${ctx}/res/css/main.css">

<script src="${ctx}/res/adminLTE/bower_components/jquery/dist/jquery.min.js"></script>

<!-- Bootstrap 3.3.7 -->
<script src="${ctx}/res/adminLTE/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
<!-- Morris.js charts -->
<script src="${ctx}/res/adminLTE/bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
<!-- datepicker -->
<script src="${ctx}/res/adminLTE/bower_components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<!-- Bootstrap WYSIHTML5 -->
<!-- Slimscroll -->
<script src="${ctx}/res/adminLTE/bower_components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<#--<script src="${ctx}/resources/adminLTE/bower_components/fastclick/lib/fastclick.js"></script>-->
<!-- AdminLTE App -->
<script src="${ctx}/res/adminLTE/dist/js/adminlte.min.js"></script>
<!-- AdminLTE dashboard demo (This is only for demo purposes) -->
<#--<script src="${ctx}/resources/adminLTE/dist/js/pages/dashboard.js"></script>-->
<!-- AdminLTE for demo purposes -->
<script src="${ctx}/res/adminLTE/dist/js/demo.js"></script>
<script src="${ctx}/res/layer/layer.js"></script>
<#--<script src="${ctx}/resources/adminLTE/bower_components/PACE/pace.min.js"></script>-->
<script src="${ctx}/res/knockout/knockout-3.5.0.js"></script>
<script src="${ctx}/res/adminLTE/bower_components/select2/dist/js/select2.full.min.js"></script>
<script type="text/javascript">
    function initMenu(menuId) {
        let parent = $("#" + menuId).parent().parent().parent();
        $(".sidebar-menu").find(".active").removeClass("active menu-open");
        parent.addClass("active menu-open");
        $("#" + menuId).parent().addClass("active");
    }

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
        return myModel.id;
    }

    function hideMyModal() {
        if (myModel.callBack != null) {
            myModel.callBack.apply(this, arguments);
        }
        layer.close(myModel.id);
    }
</script>