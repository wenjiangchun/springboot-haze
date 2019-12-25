<!DOCTYPE html>
<html>
<head>
<title>车辆故障信息</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">统计查询</a></li>
				<li class="active">车辆故障日志</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">车辆故障日志列表</h3>
							<div class="box-tools">
							</div>
								<form class="form-inline">
									<div class="box-body">
										<div class="col-sm-3 m-b-xs">
											<div class="form-group">
												<label class="control-label">VIN</label>
												<div class="control-div">
													<input type="text" class="form-control" name="vin_like" data-bind="value: vin">
												</div>
											</div>
										</div>
										<div class="col-sm-3 m-b-xs">
											<div class="form-group">
												<label class="control-label">开始时间</label>
												<div class="control-div" style="width: 220px">
													<input type="date" placeholder="开始日期" class="form-control" name="" data-bind="value: startDay">
												</div>
											</div>
										</div>
										<div class="col-sm-3 m-b-xs">
											<div class="form-group">
												<label class="control-label">结束时间</label>
												<div class="control-div" style="width: 220px">
													<input type="date" placeholder="结束日期" class="form-control" name="" data-bind="value: endDay">
												</div>
											</div>
										</div>
										<button type="button" class="btn btn-sm btn-primary" data-bind='click: query' style="margin-left:5px;">
											<i class="fa fa-search"></i> 查询
										</button>
										<button type="button" class="btn btn-sm btn-warning" data-bind='click: exportExcel' style="margin-left:10px;"><i class="fa fa-file-excel-o"></i> 导出</button>
									</div>
								</form>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table id="contentTable" class="table table-bordered table-striped table-hover">
								<thead>
								<tr>
									<th sName="id">编号</th>
									<th sName="vin">VIN</th>
									<th sName="busNum">公交自编号</th>
									<th sName="drivingNum">车牌号</th>
									<th sName="groupName">运营线路</th>
									<th sName="rootGroupName">运营公司</th>
									<th sName="logTime">故障时间</th>
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
			startDay: ko.observable(),
			endDay:ko.observable(),
			vin: ko.observable(),
			query: function() {
				refreshTable();
			},
			exportExcel: function() {
				let url = "${ctx}/v/stat/exportExcel?type=2";
				if (this.startDay() != undefined) {
					url += '&startDay=' + this.startDay();
				}
				if (this.endDay() != undefined) {
					url += '&endDay=' + this.endDay();
				}
				window.location.href=url;
			}
		};
		ko.applyBindings(viewModel);
		initDataTable();
	});


	function initDataTable() {
		const options = {
			divId : "contentTable",
			url : "${ctx}/v/stat/searchBreakdown"
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
