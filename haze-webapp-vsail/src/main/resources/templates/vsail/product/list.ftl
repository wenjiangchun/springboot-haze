<!DOCTYPE html>
<html>
<head>
<title>${cname!}管理</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="${ctx}/"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">车辆管理</a></li>
				<li class="active">${cname!}管理</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">${cname!}列表</h3>
							<div class="box-tools">
							</div>
								<form class="form-inline">
									<div class="box-body">
										<div class="form-group">
											<label for="name_like">${cname!}名称</label>
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
									<th sName="name">产品名称</th>
									<th sName="supplyName">供应商名称</th>
									<th sName="supplyAddress">供应商地址</th>
									<th sName="linker">联系人</th>
									<th sName="mobile">电话</th>
									<th sName="description">描述</th>
									<th sName="operate" columnRender="formatOperator">操作</th>
								</tr>
								</tr>
								</thead>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
					<!-- /.box -->
					<a href="#" class="btn btn-info" data-bind='click: add'><i class="fa fa-plus-circle"></i>  添加${cname!}</a>
				</div>
				<!-- /.col -->
			</div>
			<!-- /.row -->
		</section>
</body>
<script type="text/javascript">
	let viewModel;
	$(document).ready(function() {
		initMenu('v_${name!}_menu');
		viewModel = {
			dictName: ko.observable(''),
			add: function() {
				let url = "${ctx}/v/${name!}/add";
				showMyModel(url,'添加${cname!}', '900px', '50%', callBackAction);
			},
			reset: function() {
				$(".datatable_query").val('');
			},
			query: function() {
				refreshTable();
			},
			edit: function(id) {
				let url = "${ctx}/v/${name!}/edit/" + id;
				showMyModel(url,'编辑${cname!}', '900px', '50%', callBackAction);
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
							url:'${ctx}/v/${name!}/delete/'+ids,
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
			url : "${ctx}/v/${name!}/search"
		};
		createTable(options);
	}

	function formatOperator(data) {
		let html = "";
		html += "<a href='javascript:void(0)' onclick='viewModel.edit(" + data.id + ")' title='编辑'> <i class='fa fa-edit fa-lg'></i> </a> | ";
		html += "<a href='javascript:void(0)' onclick='viewModel.delete(" + data.id + ")' title='删除'> <i class='fa fa-trash-o fa-lg'></i> </a>";
		return html;
	}

	function callBackAction(data) {
		refreshTable();
	}
</script>
</html>
