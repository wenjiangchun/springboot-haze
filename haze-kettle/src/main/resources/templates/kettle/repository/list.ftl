<!DOCTYPE html>
<html>
<head>
  <#include "../../common/head.ftl"/>
    <link rel="stylesheet" href="${ctx.contextPath}/resources/adminLTE/bower_components/datatables.net-bs/css/dataTables.bootstrap.min.css">
    <link rel="stylesheet" href="${ctx.contextPath}/resources/adminLTE/bower_components/Ionicons/css/ionicons.min.css">
    <link href="${ctx.contextPath}/resources/zTree/css/bootstrapStyle/bootstrapStyle.css" type="text/css" rel="stylesheet" />

</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
<#include "../../common/nav.ftl"/>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                资源库管理
                <small>资源库列表</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> 主页</a></li>
                <li><a href="#">ETL管理</a></li>
                <li class="active">资源库管理</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">资源库列表</h3>
                            <div class="box-tools">
                                <a href="#" id="refreshRepository" class="btn btn-default"><i class="fa fa-repeat"></i>  刷新</a>
                                <a href="#" id="addRepository" class="btn btn-info"><i class="fa fa-plus-circle"></i>  添加</a>
                            </div>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <table id="example1" class="table table-bordered table-striped table-hover">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>资源库名称</th>
                                    <th>资源库类型</th>
                                    <th>URL</th>
                                    <th>IP</th>
                                    <th>PORT</th>
                                    <th>用户名</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <#list kettleRepositoryList as repo>
                                <tr>
                                    <td>${repo.id!}</td>
                                    <td>${repo.name!}</td>
                                    <td>${repo.dbType!}</td>
                                    <td>${repo.url!}</td>
                                    <td>${repo.host!}</td>
                                    <td>${repo.port!}</td>
                                    <td>${repo.userName!}</td>
                                    <td>
                                        <a href="javascript:void(0)"
                                           onclick="testConnection(${repo.id})"
                                           class="btn btn-primary btn-sm" title="测试"><i class="fa fa-arrows-h"></i></a>
                                        <a href="javascript:void(0)"
                                           onclick="createSchema(${repo.id})"
                                           class="btn btn-primary btn-sm" title="创建资源库表"><i class="fa fa-database"></i></a>
                                        <a href="javascript:void(0)"
                                           onclick="showElementInfo(${repo.id}')"
                                           class="btn btn-primary btn-sm" title="编辑"><i class="fa fa-pencil"></i></a>
                                        <a href="javascript:void(0)"
                                           onclick="showElementInfo(${repo.id}')"
                                           class="btn btn-danger btn-sm" title="删除"><i class="fa fa-trash"></i></a>
                                    </td>
                                </tr>
                                </#list>
                                </tbody>
                            </table>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                </div>
                <!-- /.col -->
            </div>
            <!-- /.row -->
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
<#include "../../common/foot.ftl"/>
<#include "../../common/left.ftl"/>
</div>
<script src="${ctx.contextPath}/resources/adminLTE/bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${ctx.contextPath}/resources/adminLTE/bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script src="${ctx.contextPath}/resources/adminLTE/bower_components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script src="${ctx.contextPath}/resources/adminLTE/bower_components/fastclick/lib/fastclick.js"></script>
<script type="text/javascript" src="${ctx.contextPath}/resources/zTree/js/jquery.ztree.all.js"></script>
<script>
    $(function () {
        initMenu('etl_repos_menu');
        $('#example1').DataTable({
            'paging': true,
            'lengthChange': true,
            'searching': true,
            'ordering': false,
            'info': true,
            'autoWidth': true,
            "oLanguage": {
                "sLengthMenu": "每页显示 _MENU_条记录",
                "sZeroRecords": "没有检索到数据",
                "sInfo": "显示第 _START_ - _END_ 条记录；共 _TOTAL_ 条记录",
                "sInfoEmpty": "",
                "sProcessing": "正在加载数据...",
                "sSearch": "检索：",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "上一页",
                    "sNext": "下一页",
                    "sLast": '尾页'
                }
            },
            "columnDefs": [
                     { "width": "20%", "targets": [ 4 ] },
                     { "ordering": "true", "targets": [ 2 ] }
                   ]
            });

        $("#refreshRepository").click(function() {
            window.location.href="${ctx.contextPath}/kettle/refreshRepository";
        });
        $("#addRepository").click(function() {
            layer.open({
                type: 2,
                title: '新增资源库',
                shadeClose: true,
                shade: 0.8,
                area: ['800px', '60%'],
                content: '${ctx.contextPath}/kettle/repository/add'
            });
        });
    });


    function testConnection(id) {
        $.post('${ctx.contextPath}/kettle/repository/testConnection/' + id,function(data){
            layer.alert(data.content)
        });
    }

    function createSchema(id) {
        layer.load();
        $.post('${ctx.contextPath}/kettle/repository/createSchema/' + id,function(data){
            layer.closeAll('loading');
            layer.alert(data.content)
        });
    }

    function showElementLog(objectId, title, type) {
        layer.open({
            type: 2,
            title: title,
            shadeClose: true,
            shade: 0.8,
            area: ['80%', '60%'],
            content: '${ctx.contextPath}/kettle/trans/getElementLog/' + objectId + '/' + type //iframe的url
        });
    }

    function previewTrans(objectId, title, type) {
        layer.open({
            type: 2,
            title: title,
            shadeClose: true,
            shade: 0.8,
            area: ['80%', '70%'],
            content: '${ctx.contextPath}/kettle/trans/previewTrans/' + objectId + '/' + type //iframe的url
        });
    }
</script>
</body>

</html>
