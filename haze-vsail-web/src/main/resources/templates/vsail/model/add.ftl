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
                                <label for="name" class="col-sm-2 control-label">${cname!}名称</label>
                                <div class="col-sm-10">
                                    <input type="text" id="name" name="name" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="address" class="col-sm-2 control-label">主机厂信息</label>
                                <div class="col-sm-10">
                                    <select name="busEngine.id" class="form-control" required>
                                        <#list engineList as engine>
                                            <option value="${engine.id}">${engine.name}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="leftPos" class="col-sm-2 control-label">左侧册土</label>
                                <div class="col-sm-10">
                                    <input type="text" name="leftPos.num" class="form-control" maxlength="200" required/>
                                    <input type="text" name="leftPos.x" class="form-control" maxlength="200" required/>
                                    <input type="text" name="leftPos.y" class="form-control" maxlength="200" required/>
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
