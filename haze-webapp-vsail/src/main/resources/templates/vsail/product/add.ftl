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
                        <h3 class="box-title">${cname!}信息</h3>
                    </div>
                    <form id="inputForm" class="form-horizontal" action="${ctx}/v/${name}/save/" method="post">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="productNum" class="col-sm-2 control-label">主机ID</label>
                                <div class="col-sm-10">
                                    <input type="text" id="productNum" name="productNum" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="vin" class="col-sm-2 control-label">VIN</label>
                                <div class="col-sm-10">
                                    <input type="text" id="vin" name="vin" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="busNum" class="col-sm-2 control-label">车辆自编号</label>
                                <div class="col-sm-10">
                                    <input type="text" id="busNum" name="busNum" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="control" class="col-sm-2 control-label">控制模块</label>
                                <div class="col-sm-10">
                                    <input type="text" id="control" name="control" class="form-control" maxlength="50" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="supplyAddress" class="col-sm-2 control-label">显示模块</label>
                                <div class="col-sm-10">
                                    <input type="text" id="display" name="display" class="form-control" maxlength="200" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="probe" class="col-sm-2 control-label">探测启动模块</label>
                                <div class="col-sm-10">
                                    <input type="text" id="probe" name="probe" class="form-control" maxlength="50" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="outfire" class="col-sm-2 control-label">气溶胶灭火装置</label>
                                <div class="col-sm-10">
                                    <input type="text" id="outfire" name="outfire" class="form-control" maxlength="50" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="line" class="col-sm-2 control-label">感温电缆</label>
                                <div class="col-sm-10">
                                    <input type="text" id="line" name="line" class="form-control" maxlength="50" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="transfer" class="col-sm-2 control-label">智能传输模块</label>
                                <div class="col-sm-10">
                                    <input type="text" id="transfer" name="transfer" class="form-control" maxlength="50" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="sim" class="col-sm-2 control-label">SIM卡</label>
                                <div class="col-sm-10">
                                    <input type="text" id="sim" name="sim" class="form-control" maxlength="50" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="description" class="col-sm-2 control-label">描述:</label>
                                <div class="col-sm-10">
                                    <textarea rows="3" id="description" name="description" class="form-control" maxlength="200"></textarea>
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
