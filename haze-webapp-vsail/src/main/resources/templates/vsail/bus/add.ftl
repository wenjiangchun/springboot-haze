<!DOCTYPE html>
<html>
<head>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">

<#macro buildNode childs>
    <#if childs?? && childs?size gt 0>
        <#list childs as child>
            <option value="${child.id}" <#if parent.id==child.id>selected</#if>>
                ${child.name}
            </option>
            <#assign depth = depth + 1 />
            <@buildNode childs=child.childs />
            <#assign depth = depth - 1 />
        </#list>
    </#if>
</#macro>

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
                                <label for="root.id" class="col-sm-2 control-label">所属公司或线路:</label>
                                <div class="col-sm-10" >
                                    <#assign depth = 1 />
                                    <select name="group.id" class="form-control">
                                        <option value="${root.id}" <#if parent.id==root.id>selected</#if>>
                                            ${(root.name)}
                                        </option>
                                        <#list root.childs as group>
                                            <option value="${group.id}" <#if parent.id==group.id>selected</#if>>
                                                ${group.name}
                                            </option>
                                            <@buildNode childs=group.childs />
                                        </#list>
                                    </select>
                                    <input type="hidden" name="rootGroup.id" class="form-control" maxlength="20" value="${root.id}"/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="address" class="col-sm-2 control-label">车辆型号</label>
                                <div class="col-sm-10">
                                    <select name="busModel.id" class="form-control" required>
                                        <#list modelList as engine>
                                            <option value="${engine.id}">${engine.name}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="vin" class="col-sm-2 control-label">vin</label>
                                <div class="col-sm-10">
                                    <input type="text"  name="vin" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="busNum" class="col-sm-2 control-label">busNum</label>
                                <div class="col-sm-10">
                                    <input type="text"  name="busNum" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="registNum" class="col-sm-2 control-label">registNum</label>
                                <div class="col-sm-10">
                                    <input type="text"  name="registNum" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="engineNum" class="col-sm-2 control-label">engineNum</label>
                                <div class="col-sm-10">
                                    <input type="text" name="engineNum" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="drivingNum" class="col-sm-2 control-label">drivingNum</label>
                                <div class="col-sm-10">
                                    <input type="text" name="drivingNum" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="motorName" class="col-sm-2 control-label">motorName</label>
                                <div class="col-sm-10">
                                    <input type="text" name="motorName" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="motorNum" class="col-sm-2 control-label">motorNum</label>
                                <div class="col-sm-10">
                                    <input type="text" name="motorNum" class="form-control" maxlength="20" required/>
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
