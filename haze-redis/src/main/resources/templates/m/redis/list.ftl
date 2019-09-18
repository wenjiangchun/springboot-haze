<!DOCTYPE html>
<html>
<head>
<title>redis管理</title>
	<#include "../../common/head.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
	<#include "../../common/nav.ftl"/>
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>
				Redis查看
				<small>redis信息</small>
			</h1>
			<ol class="breadcrumb">
				<li><a href="${ctx}/"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">监控管理</a></li>
				<li class="active">Redis查看</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-3">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">所有key</h3>
						</div>
						<div class="box-body">
							<ul class="nav nav-pills nav-stacked">
								<#list keys as key>
									<li>
										<a href="#" data-bind="event: {click: getKey.bind($data, '${key}') }"><i class="fa fa-circle-o text-red"></i> ${key}</a>
									</li>
								</#list>
							</ul>
						</div>
						<!-- /.box-body -->
					</div>
				</div>
				<div class="col-xs-9">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">值 <span data-bind="text: key"></span></h3>
							<div class="box-tools">
							</div>

						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<label>
								<textarea class="form-control" data-bind="value: val"></textarea>
							</label>
						</div>
						<!-- /.box-body -->
					</div>
					<!-- /.box -->
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
		initMenu('monitor_redis_menu');
		viewModel = {
			key: ko.observable(''),
			val: ko.observable(''),
			getKey: function(key) {
				this.key(key);
				const url = "${ctx}/m/redis/get/" + key;
				const that = this;
				$.get(url, function(data) {
					//that.val(JSON.stringify(data));
					that.val(data);
				});
		    }
		};
		ko.applyBindings(viewModel);
	});
</script>
</html>
