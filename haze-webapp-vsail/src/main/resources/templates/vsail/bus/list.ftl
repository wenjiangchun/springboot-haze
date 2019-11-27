<!DOCTYPE html>
<html>
<head>
<title>${cname!}管理</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
	<#include "../../common/ztree.ftl"/>
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
				<div class="col-xs-3">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">运营公司</h3>
						</div>
						<div class="box-body">
							<ul id="groupTree" class="ztree"></ul>
						</div>
						<!-- /.box-body -->
					</div>
				</div>
				<div class="col-xs-9">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">${cname!}列表<span data-bind="text: groupName"></span></h3>
							<div class="box-tools">
							</div>
								<form class="form-inline">
									<input type="hidden" class="datatable_query" name="parent.id" data-bind="value: groupId"/>
									<div class="box-body">
										<div class="form-group">
											<label for="vin_like">vin码</label>
											<input type="text" name="vin_like" class="datatable_query form-control">
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
									<th sName="vin">vin</th>
									<th sName="busModel.name">型号</th>
									<th sName="rootGroup.fullName">运营公司</th>
									<th sName="group.fullName" columnRender="formatGroup">所属子公司/线路</th>
									<th sName="operate" columnRender="formatOperator">操作</th>
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
	let tree;
	$(document).ready(function() {
		viewModel = {
			groupName: ko.observable(''),
			groupId: ko.observable('${parentId!}'),
			add: function() {
				if (this.groupId() == null || this.groupId() === '') {
					layer.alert('请选择所属运营公司')
				} else {
					let url = "${ctx}/v/${name}/add?parentId=" + this.groupId();
					showMyModel(url,'添加${cname!}', '900px', '50%', callBackAction);
				}
			},
			reset: function() {
				$(".datatable_query").val('');
			},
			query: function() {
				refreshTable();
			},
			edit: function(id) {
				let url = "${ctx}/v/${name}/edit/" + id;
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
							url:'${ctx}/v/${name}/delete/'+ids,
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
		initGroupTree();
	});


	function initDataTable() {
		const options = {
			divId : "contentTable",
			url : "${ctx}/v/${name}/search"
		};
		createTable(options);
	}

	function initGroupTree() {
		$.ajax({
			method : "post",
			url : "${ctx}/v/${name}/getBusGroups",
			dataType : "json",
			success : function(data) {
				var setting = {data:{
						simpleData:{
							enable:true,
							idKey:"id",
							pIdKey:"pid",
							rootPId:null
						},
						key:{
							name:"fullName"
						}
					}, callback: {
						onClick:onClick
					}};
				$.fn.zTree.init($("#groupTree"), setting, data);

				tree = $.fn.zTree.getZTreeObj("groupTree");
				var parentId = viewModel.groupId();
				if (parentId != null && parentId != "") {
					var node = tree.getNodeByParam("id",parentId);
					if(!node.isParent){
						node = node.getParentNode();
					}
					tree.selectNode(node,false);
					tree.expandNode(node, true, false, true);
				} else {
					var node = tree.getNodeByParam("fullName","运营公司");
					tree.expandNode(node, true, false, true);
				}
			}
		});
	}

	function onClick(event, treeId, treeNode, clickFlag) {
		tree.expandNode(treeNode, true, false, true);
		let fullName = treeNode.fullName!= null && treeNode.fullName !== '运营公司' ? '(' + treeNode.fullName + ')': '';
		viewModel.groupId(treeNode.id);
		viewModel.groupName(fullName);
		refreshTable();
	}

	function formatOperator(data) {
		let html = "";
		html += "<a href='javascript:void(0)' onclick='viewModel.edit(" + data.id + ")' title='编辑'> <i class='fa fa-edit fa-lg'></i> </a> | ";
		html += "<a href='javascript:void(0)' onclick='viewModel.delete(" + data.id + ")' title='删除'> <i class='fa fa-trash-o fa-lg'></i> </a>";
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
