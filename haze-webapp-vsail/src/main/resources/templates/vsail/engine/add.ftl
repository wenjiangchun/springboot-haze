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
                    <form id="inputForm" class="form-horizontal" action="${ctx}/v/engine/save/" method="post">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="name" class="col-sm-2 control-label">${cname!}名称</label>
                                <div class="col-sm-10">
                                    <input type="text" id="name" name="name" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="address" class="col-sm-2 control-label">${cname!}地址</label>
                                <div class="col-sm-10">
                                    <input type="text" id="address" name="address" class="form-control" maxlength="200" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="linker" class="col-sm-2 control-label">联系人</label>
                                <div class="col-sm-10">
                                    <input type="text" id="linker" name="linker" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="mobile" class="col-sm-2 control-label">联系电话</label>
                                <div class="col-sm-10">
                                    <input type="text" id="mobile" name="mobile" class="form-control" maxlength="11" required/>
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
