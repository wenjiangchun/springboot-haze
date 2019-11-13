<!DOCTYPE html>
<html>
<head>
<title>上线下线信息</title>
	<#include "../../common/v-head.ftl"/>
	<#include "../../common/datatable.ftl"/>
	<style>
		.wrapper-content {
			padding: 20px;
			overflow: auto;
		}
	</style>
</head>
<body >
<div id="wrapper">
	<!--左侧导航开始-->

	<!--左侧导航结束-->
	<!--右侧部分开始-->


	<div class="row J_mainContent" id="content-main">
		<div class="wrapper wrapper-content animated fadeInRight wrapper-background">
			<div class="row">
				<div class="col-sm-12">
					<div class="ibox ibox-background float-e-margins">
						<div class="ibox-title">
							<h5><img src="img/title.png">自定义响应式表格</h5>

						</div>
						<div class="ibox-content">
							<div class="row">
								<div class="col-sm-2 m-b-xs">
									<div class="form-group">
										<label class="control-label">VIN码</label>
										<div class="control-div">
											<select class="form-control m-b" name="account">
												<option>选项 1</option>
												<option>选项 2</option>
												<option>选项 3</option>
												<option>选项 4</option>
											</select>
										</div>
									</div>
								</div>
								<div class="col-sm-2 m-b-xs">
									<div class="form-group">
										<label class="control-label">公交自编号</label>
										<div class="control-div">
											<input type="text" class="form-control" name="text">
										</div>
									</div>
								</div>
								<div class="col-sm-2 m-b-xs">
									<div class="form-group">
										<label class="control-label">开始时间</label>
										<div class="control-div">
											<input type="" placeholder="选择开始日期" class="form-control" name="">
										</div>
									</div>
								</div>
								<div class="col-sm-2 m-b-xs">
									<div class="form-group">
										<label class="control-label">结束时间</label>
										<div class="control-div">
											<input type="" placeholder="选择结束日期" class="form-control" name="">
										</div>
									</div>
								</div>
								<div class="form-button">
									<button type="button" class="btn btn-w-m btn-info"><img src="img/btn1.png">查询</button>
									<button type="button" class="btn btn-w-m btn-warning"><img src="img/btn2.png">导出数据</button>
								</div>

							</div>
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
									<tr>

										<th>序号</th>
										<th>编号</th>
										<th>vin码</th>
										<th>录入时间</th>
										<th>登陆情况</th>

									</tr>
									</thead>
									<tbody>
									<tr>
										<td>1</td>
										<td>165313424
										</td>
										<td>BJSRDZNMKV1010012</td>
										<td>2019-10-28 14:54:50</td>
										<td>
											<a href="table_basic.html#"><i><img src="img/k1.png"></i>已登陆</a>
										</td>
									</tr>
									</tbody>
								</table>
							</div>
							<nav aria-label="Page navigation" style="text-align: right;">
								<ul class="pagination">
									<li>
										<a href="#" aria-label="Previous">
											<span aria-hidden="true">&laquo;</span>
										</a>
									</li>
									<li><a href="#">1</a></li>
									<li><a href="#">2</a></li>
									<li><a href="#">3</a></li>
									<li><a href="#">4</a></li>
									<li><a href="#">5</a></li>
									<li>
										<a href="#" aria-label="Next">
											<span aria-hidden="true">&raquo;</span>
										</a>
									</li>
								</ul>
							</nav>

						</div>
					</div>
				</div>

			</div>
		</div>

	</div>
</div>
<!--右侧部分结束-->
</div>
<script>
	$(".nav-content li").click(function() {
		$(".sortable-list-box").show();
		$(".div-dw").show();
	})
	$(".btn.btn-sm.btn-white").click(function() {
		$(".sortable-list-box").show();
		$(".div-dw").show();
	})
	$(".div-dw").click(function() {
		$(this).hide();
		$(".sortable-list-box").hide();
	})
</script>
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
			url : "${ctx}/v/stat/searchOnOff"
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
