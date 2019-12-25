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
                    <h3 class="box-title">运营信息</h3>
                </div>
                <form id="inputForm" class="form-horizontal" action="${ctx}/v/${name}/saveBus/" method="post" onsubmit="return viewModel.validator()">
                    <div class="box-body">
                        <div class="form-group db">
                            <label for="vin" class="col-sm-2 control-label">VIN</label>
                            <div class="col-sm-10">
                                <p class="form-control-static">${bus.vin!}</p>
                            </div>
                        </div>
                        <div class="form-group db">
                            <label for="busNum" class="col-sm-2 control-label">车辆自编号</label>
                            <div class="col-sm-10">
                                <p class="form-control-static">${bus.busNum!}</p>
                                <input type="hidden" name="id" value="${bus.id!}">
                                <input type="hidden" name="eventCode" value="5">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="lineGroup.fullName" class="col-sm-2 control-label">所属线路:</label>
                            <div class="col-sm-10">
                                <p class="form-control-static">${bus.lineGroup.parent.fullName}->${bus.lineGroup.fullName}</p>
                                <input type="hidden" name="lineGroup.id" class="form-control" maxlength="20" value="${bus.lineGroup.id}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="drivingNum" class="col-sm-2 control-label">牌照号</label>
                            <div class="col-sm-10">
                                <input type="text"  name="drivingNum" class="form-control" maxlength="20" required placeholder="牌照号" value="${bus.drivingNum!}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="productNum" class="col-sm-2 control-label">主机ID</label>
                            <div class="col-sm-10">
                                <input type="text"  name="productNum" class="form-control" maxlength="20" required placeholder="主机ID" value="${bus.productNum!}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="productBrand" class="col-sm-2 control-label">设备品牌</label>
                            <div class="col-sm-10">
                                <input type="text"  name="productBrand" class="form-control" maxlength="20" required value="福赛尔" placeholder="设备品牌" value="${bus.productBrand!}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="productType" class="col-sm-2 control-label">设备类型</label>
                            <div class="col-sm-10">
                                <input type="text"  name="productType" class="form-control" maxlength="20" required value="电池监控及抑制" placeholder="设备类型" value="${bus.productType!}"/>
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
