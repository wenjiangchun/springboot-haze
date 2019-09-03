<!DOCTYPE html>
<html>
<head>
<title>配置管理</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
	<#include "../../common/nav.ftl"/>
	<div class="content-wrapper">
		<section class="content-header">
			<h1>
				配置管理
				<small><i class="fa fa-wrench"></i></small>
			</h1>
			<ol class="breadcrumb">
				<li><a href="${ctx}/"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">系统管理</a></li>
				<li class="active">配置管理</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">配置列表</h3>
							<div class="box-tools">
								<#--<a href="#" id="refreshRepository" class="btn btn-default"><i class="fa fa-repeat"></i>  刷新</a>
								<a href="#" id="add_btn" class="btn btn-info"><i class="fa fa-plus-circle"></i>  添加</a>-->
							</div>
								<form class="form-inline">
									<input type="hidden" class="datatable_query" name="parent.id" data-bind="value: parentId"/>
									<div class="box-body">
										<div class="form-group">
											<label for="name_like">配置名称</label>
											<input type="text" name="name_like" class="datatable_query form-control">
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
								<tr>
									<th sName="id">编号</th>
									<th sName="name">配置名称</th>
									<th sName="code">配置代码</th>
									<th sName="value">配置值</th>
									<th sName="configType" columnRender="formatConfigType">配置类型</th>
									<th sName="description">配置说明</th>
									<th sName="operate" columnRender="formatOperator">操作</th>
								</tr>
								</tr>
								</thead>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
					<!-- /.box -->
					<a href="#" class="btn btn-info" data-bind='click: add'><i class="fa fa-plus-circle"></i>  添加配置</a>
				</div>
				<!-- /.col -->
			</div>
			<!-- /.row -->
		</section>
	</div>
</div>
</body>
<script type="text/javascript">
	let viewModel;
	$(document).ready(function() {
		initMenu('sys_config_menu');
		viewModel = {
			dictName: ko.observable(''),
			parentId: ko.observable('${parentId!}'),
			add: function() {
				let url = "${ctx}/system/config/add";
				if (this.parentId() != null && this.parentId() !== "") {
					url += "?parentId=" + this.parentId();
				}
				showMyModel(url,'添加配置', '900px', '50%', callBackAction);
			},
			reset: function() {
				$(".datatable_query").val('');
			},
			query: function() {
				refreshTable();
			},
			edit: function(id) {
				let url = "${ctx}/system/config/edit/" + id;
				showMyModel(url,'编辑配置', '900px', '50%', callBackAction);
			},
			delete: function(id) {
				if (id == null || id === "") {
					alert("ID不能为空");
				} else {
					layer.confirm('确认删除?', {
						btn: ['确认','取消'] //按钮
					}, function(){
						const ids = [id];
						$.post({
							url:'${ctx}/system/config/delete/'+ids,
							success:function(data) {
								if (data.messageType == 'SUCCESS') {
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
			url : "${ctx}/system/config/search"
		};
		createTable(options);
	}

	function formatConfigType(data) {
		if (data != null) {
			if (data.configType == 'B') {
				return "业务配置";
			} else if (data.configType == 'S') {
				return "系统配置";
			}
		}
		return null;
	}
	function formatOperator(data) {
		let html = "";
		html += "<a href='javascript:void(0)' onclick='viewModel.edit(" + data.id + ")' title='编辑'> <i class='fa fa-edit fa-lg'></i> </a> | ";
		if (data.configType == 'B') {
			html += "<a href='javascript:void(0)' onclick='viewModel.delete(" + data.id + ")' title='删除'> <i class='fa fa-trash-o fa-lg'></i> </a>";
		}
		return html;
	}

	function callBackAction(data) {
		refreshTable();
	}
</script>
</html>
