<!DOCTYPE html>
<html>
<head>
<title>车辆管理</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
	<#include "../../common/ztree.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">基础数据管理</a></li>
				<li class="active">车辆管理</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">车辆列表<span data-bind="text: groupName"></span></h3>
							<div class="box-tools">
							</div>
								<form class="form-inline">
									<div class="box-body">
										<div class="form-group">
											<label for="vin_like">VIN</label>
											<input type="text" name="vin_like" class="datatable_query form-control">
										</div>
										<div class="form-group">
											<label for="busNum_like">车辆自编号</label>
											<input type="text" name="busNum_like" class="datatable_query form-control">
										</div>
										<button type="button" class="btn btn-sm btn-primary" data-bind='click: query' style="margin-left:5px;">
											<i class="fa fa-search"></i> 查询
										</button>
										<button type="button" class="btn btn-sm btn-danger" data-bind='click: reset' style="margin-left:10px;">清空</button>
									</div>
								</form>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table id="contentTable" class="table table-bordered table-striped table-hover">
								<thead>
								<tr>
									<th sName="id">编号</th>
									<th sName="vin" bSortable="true">VIN</th>
									<th sName="busNum" bSortable="true">车辆自编号</th>
									<th sName="modelName">车辆型号</th>
									<th sName="assembleDay">装车时间</th>
									<th sName="factoryName">主机厂名称</th>
									<th sName="assembleAddress">装车地点</th>
									<th sName="operate" columnRender="formatOperator">操作</th>
								</tr>
								</thead>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
					<!-- /.box -->
					<a href="#" class="btn btn-info" data-bind='click: add'><i class="fa fa-plus-circle"></i>  添加车辆</a>
				</div>
				<!-- /.col -->
			</div>
			<!-- /.row -->
		</section>
</body>
<script type="text/javascript">
	let viewModel;
	let tree;
	$(document).ready(function() {
		viewModel = {
			groupName: ko.observable(''),
			add: function() {
				let url = "${ctx}/v/bus/addInfo";
				showMyModel(url,'添加车辆', '900px', '60%', callBackAction);
			},
			reset: function() {
				$(".datatable_query").val('');
			},
			query: function() {
				refreshTable();
			},
			edit: function(id) {
				let url = "${ctx}/v/bus/editInfo/" + id;
				showMyModel(url,'编辑车辆', '900px', '50%', callBackAction);
			},
			delete: function(id) {
				if (id == null || id === "") {
					layer.alert('ID不能为空');
				} else {
					layer.confirm('确认删除?', {
						btn: ['确认','取消'] //按钮
					}, function(){
						const ids = [id];
						$.post({
							url:'${ctx}/v/bus/delete/'+ids,
							success:function(data) {
								if (data.messageType === 'SUCCESS') {
									layer.alert('删除成功');
									callBackAction(data);
								} else {
									layer.alert('删除失败:' + data.content);
								}
							}
						});
					}, function(){
					});
				}
			}
		};
		ko.applyBindings(viewModel);
		initDataTable();
	});


	function initDataTable() {
		const options = {
			divId : "contentTable",
			url : "${ctx}/v/bus/search"
		};
		createTable(options);
	}

	function formatOperator(data) {
		let html = "";
		html += "<a href='javascript:void(0)' onclick='viewModel.edit(" + data.id + ")' title='编辑'> <i class='fa fa-edit fa-lg'></i> </a>";
		if (!data.used) {
			html += " | <a href='javascript:void(0)' onclick='viewModel.delete(" + data.id + ")' title='删除'> <i class='fa fa-trash-o fa-lg'></i> </a>";
		}
		return html;
	}

	function formatGroup(data) {
		if (data.group.parent != null) {
			return data.group.parent.fullName + '/' + data.group.fullName;
		} else {
			return data.group.fullName;
		}
	}
	function callBackAction(data) {
		refreshTable();
	}
</script>
</html>
