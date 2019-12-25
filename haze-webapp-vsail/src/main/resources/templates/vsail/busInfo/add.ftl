<!DOCTYPE html>
<html>
<head>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">

<section class="content">
    <div class="row">
        <div class="col-xs-12">
                <div class="box box-info">
                    <div class="box-header with-border">
                        <h3 class="box-title">车辆信息</h3>
                    </div>
                    <form id="inputForm" class="form-horizontal" action="${ctx}/v/bus/saveBusInfo/" method="post">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="vin" class="col-sm-2 control-label">VIN</label>
                                <div class="col-sm-10">
                                    <input type="text"  name="vin" class="form-control" maxlength="20" required placeholder="请输入VIN码"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="busNum" class="col-sm-2 control-label">车辆自编号</label>
                                <div class="col-sm-10">
                                    <input type="text"  name="busNum" class="form-control" maxlength="20" required placeholder="请输入车辆自编号"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="modelName" class="col-sm-2 control-label">车辆型号</label>
                                <div class="col-sm-10">
                                    <input type="text"  name="modelName" class="form-control" maxlength="20" required placeholder="请输入车辆型号"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="factoryName" class="col-sm-2 control-label">主机厂名称</label>
                                <div class="col-sm-10">
                                    <input type="text"  name="factoryName" class="form-control" maxlength="20" required placeholder="请输入主机厂名称"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="assembleDay" class="col-sm-2 control-label">装车时间</label>
                                <div class="col-sm-10">
                                    <input type="date"  name="assembleDay" class="form-control" maxlength="20" required placeholder="请输入装车时间"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="assembleAddress" class="col-sm-2 control-label">装配地点</label>
                                <div class="col-sm-10">
                                    <input type="text"  name="assembleAddress" class="form-control" maxlength="20" required placeholder="请输入装配地点"/>
                                </div>
                            </div>
                        </div>
                        <div class="box-footer">
                            <button type="submit" class="btn btn-primary"><i class="fa fa-check"></i> 提交</button>
                            <button type="reset" class="btn btn-danger">重置</button>
                        </div>
                    </form>
                </div>
        </div>
    </div>
</section>
<script>
    $('#inputForm').ajaxForm({
        dataType : 'json',
        success : function(data) {
            if (data.messageType === "SUCCESS") {
                layer.alert("操作成功", function() {
                    parent.hideMyModal();
                });
            } else {
                layer.alert("操作失败【"+data.content+"】");
            }
        }
    });
</script>
</body>
</html>
