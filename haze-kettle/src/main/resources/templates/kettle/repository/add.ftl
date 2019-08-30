<!DOCTYPE html>
<html>
<head>
    <#include "../../common/head.ftl"/>
    <link rel="stylesheet" href="${ctx}/resources/jointjs/joint.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content">
    <div class="row">
        <div class="col-xs-12">
                <div class="box box-info">
                    <div class="box-header with-border">
                        <h3 class="box-title">资源库信息</h3>
                    </div>
                    <form class="form-horizontal" action="${ctx}/kettle/repository/save">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="inputEmail3" class="col-sm-2 control-label">资源库名称</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" name="name" placeholder="资源库名称" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="inputPassword3" class="col-sm-2 control-label">资源库类型</label>
                                <div class="col-sm-10">
                                    <select class="form-control" name="dbType" required>
                                        <#list dbTypeList as tp>
                                                <option value="${tp.name!}" defaultPort="${tp.defaultDatabasePort?c!}">${tp.name!}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="inputEmail3" class="col-sm-2 control-label">主机IP</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" name="host" placeholder="ip">
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="inputPassword3" class="col-sm-2 control-label">schemaName</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" id="schemaName" name="schemaName" placeholder="schemaName">
                                </div>
                            </div>
                            <div class="form-group generic">
                                <label for="inputEmail3" class="col-sm-2 control-label">jdbc-url</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" id="jdbcUrl" name="jdbcUrl" placeholder="jdbc-url">
                                </div>
                            </div>
                            <div class="form-group generic">
                                <label for="inputPassword3" class="col-sm-2 control-label">driveClass</label>

                                <div class="col-sm-10">
                                    <input type="text" class="form-control" name="driveClass" placeholder="driveClass">
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="inputPassword3" class="col-sm-2 control-label">port</label>

                                <div class="col-sm-10">
                                    <input type="text" class="form-control" id="port" name="port" placeholder="port">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="inputPassword3" class="col-sm-2 control-label">userName</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" name="userName" placeholder="userName" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="inputPassword3" class="col-sm-2 control-label">password</label>
                                <div class="col-sm-10">
                                    <input type="password" class="form-control" name="password" placeholder="password">
                                </div>
                            </div>
                        </div>
                        <div class="box-footer">
                            <button type="button" class="btn btn-default" >测试连接</button>
                            <button type="submit" class="btn btn-info pull-right">保存</button>
                        </div>
                    </form>
                </div>
        </div>
    </div>
</section>
<script>
    $(function(){
        selectDb();
        $('select[name=dbType]').change(function() {
            selectDb();
        });
    });

    function selectDb() {
        var $option = $('select[name=dbType]').find("option:selected");
        var port = $option.attr('defaultPort');
        var text = $option.text();
        $('.generic').find('input').val('');
        $('.db').find('input').val('');
        if (text  === 'Generic database') {
            $('.generic').show();
            $('.generic').find('input').attr({'required':true});
            $('.db').hide();
            $('.db').find('input').removeAttr('required');
        } else {
            $('#port').val(port);
            $('.generic').hide();
            $('.generic').find('input').removeAttr('required');
            $('.db').show();
            $('.db').find('input').attr({'required':true});
        }
    }
</script>
</body>
</html>
