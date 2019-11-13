<!DOCTYPE html>
<html>
<head>
<title>上线下线信息</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<h1>
				火警日志
				<small><i class="fa fa-wrench"></i></small>
			</h1>
			<ol class="breadcrumb">
				<li><a href="${ctx}/"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">统计查询</a></li>
				<li class="active">火警日志</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">火警日志列表</h3>
							<div class="box-tools">
							</div>
								<form class="form-inline">
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
									<th sName="vin">vin码</th>
									<th sName="busNum">车辆编号</th>
									<th sName="registNum">注册编号</th>
									<th sName="engineNum">主机编号</th>
									<th sName="drivingNum">行驶证号</th>
									<th sName="groupName">运营线路</th>
									<th sName="rootGroupName">运营公司</th>
									<#--<th sName="operate" columnRender="formatOperator">操作</th>-->
								</tr>
								</thead>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
				</div>
				<!-- /.col -->
			</div>
			<!-- /.row -->
		</section>
</body>
<script type="text/javascript">
	let viewModel;
	$(document).ready(function() {
		viewModel = {
			reset: function() {
				$(".datatable_query").val('');
			},
			query: function() {
				refreshTable();
			},
			show: function(id) {
				let url = "${ctx}/v/${name!}/edit/" + id;
				showMyModel(url,'编辑${cname!}', '900px', '50%');
			}
		};
		ko.applyBindings(viewModel);
		initDataTable();
	});


	function initDataTable() {
		const options = {
			divId : "contentTable",
			url : "${ctx}/v/stat/searchFire"
		};
		createTable(options);
	}

	function formatOperator(data) {
		let html = "";
		html += "<a href='javascript:void(0)' onclick='viewModel.delete(" + data.id + ")' title='查看'> <i class='fa fa-info-circle fa-lg'></i> </a>";
		return html;
	}

	function formatFlag(data) {
		return data.flag === 0 ? "<span class='label label-success'>上线</span>" : "<span class='label label-warning'>下线</span>"
	}
</script>
</html>
