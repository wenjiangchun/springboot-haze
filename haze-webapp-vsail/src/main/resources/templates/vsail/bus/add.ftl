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
                                    <select class="form-control" id="vin" name="vin" required data-bind="event:{change: viewModel.permissionChanged}">
                                        <option>请选择</option>
                                        <#list unUsedList as bus>
                                            <option value="${bus.vin}" busId="${bus.id}" busNum="${bus.busNum}">${bus.vin}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="busNum" class="col-sm-2 control-label">车辆自编号</label>
                                <div class="col-sm-10">
                                    <p class="form-control-static" data-bind="text: busNum"></p>
                                    <input type="hidden" name="id" data-bind="value: busId">
                                    <input type="hidden" name="eventCode" value="4">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="lineGroup.fullName" class="col-sm-2 control-label">所属线路:</label>
                                <div class="col-sm-10">
                                    <p class="form-control-static">${parent.parent.fullName}->${parent.fullName}</p>
                                    <input type="hidden" name="lineGroup.id" class="form-control" maxlength="20" value="${parent.id}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="drivingNum" class="col-sm-2 control-label">牌照号</label>
                                <div class="col-sm-10">
                                    <input type="text"  name="drivingNum" class="form-control" maxlength="20" required placeholder="牌照号"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="productNum" class="col-sm-2 control-label">主机ID</label>
                                <div class="col-sm-10">
                                    <input type="text"  name="productNum" class="form-control" maxlength="20" required placeholder="主机ID"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="productBrand" class="col-sm-2 control-label">设备品牌</label>
                                <div class="col-sm-10">
                                    <input type="text"  name="productBrand" class="form-control" maxlength="20" required value="福赛尔" placeholder="设备品牌"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="productType" class="col-sm-2 control-label">设备类型</label>
                                <div class="col-sm-10">
                                    <input type="text"  name="productType" class="form-control" maxlength="20" required value="电池监控及抑制" placeholder="设备类型"/>
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

    let viewModel = {
        busId: ko.observable(),
        busNum: ko.observable(),
        permissionChanged: function(obj, event) {
            const $option = $("#vin option:selected");
            const vin = $("#vin").val();
            if (vin !== undefined) {
                const busId = $option.attr('busId');
                const busNum = $option.attr('busNum');
                this.busId(busId);
                this.busNum(busNum);
            } else {
                this.busId('');
                this.busNum('');
            }
        },
        validator: function() {
            if (this.busId() === undefined || this.busId === '') {
                layer.alert('请选择需要运营的车辆信息');
                return false;
            }
            return true;
        }
    };

    $(function() {
        ko.applyBindings(viewModel);
    });

</script>
</body>
</html>
