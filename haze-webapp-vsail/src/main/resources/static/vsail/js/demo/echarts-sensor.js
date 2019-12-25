$(function() {

	/**电池详细信息**/
	var charts = {
		unit: '户数',
		names: ['co浓度', '环境温度'],
		lineX: ['19-09-01', '19-09-02', '19-09-03', '19-09-04', '19-09-05'],
		value: [
			[2000, 5000, 9500, 6000, 3000, 6000, 10000],
			[3000, 5045, 8000, 1920, 9030, 5800]
		]
	}
	var color = ['rgba(8,125,204', 'rgba(140,154,241']
	var lineY = []

	for(var i = 0; i < charts.names.length; i++) {
		var x = i
		if(x > color.length - 1) {
			x = color.length - 1
		}
		var data = {
			name: charts.names[i],
			type: 'line',
			color: color[x] + ')',
			smooth: true,
			areaStyle: {
				normal: {
					color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
						offset: 0,
						yAxisIndex: 0,
						color: color[x] + ', 0.9)'
					}, {
						offset: 0.8,
						yAxisIndex: 1,
						color: color[x] + ', 0.9)'
					}], false),
					shadowColor: 'rgba(0, 0, 0, 0.1)',
					shadowBlur: 10
				}
			},
			symbol: 'circle',
			symbolSize: 5,
			data: charts.value[i]
		}
		lineY.push(data)
	}


	/*****分级统计****/
	var charts = {
		unit: '户数',
		names: ['co浓度', '环境温度'],
		lineX: ['19-09-01', '19-09-02', '19-09-03', '19-09-04', '19-09-05'],
		value: [
			[2000, 5000, 9500, 6000, 3000, 6000, 10000],
			[3000, 5045, 8000, 1920, 9030, 5800]
		]
	}
	var color = ['rgba(8,125,204', 'rgba(140,154,241']
	var lineY = []

	for(var i = 0; i < charts.names.length; i++) {
		var x = i
		if(x > color.length - 1) {
			x = color.length - 1
		}
		var data = {
			name: charts.names[i],
			type: 'line',
			color: color[x] + ')',
			smooth: true,
			areaStyle: {
				normal: {
					color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
						offset: 0,
						yAxisIndex: 0,
						color: color[x] + ', 0.9)'
					}, {
						offset: 0.8,
						yAxisIndex: 1,
						color: color[x] + ', 0.9)'
					}], false),
					shadowColor: 'rgba(0, 0, 0, 0.1)',
					shadowBlur: 10
				}
			},
			symbol: 'circle',
			symbolSize: 5,
			data: charts.value[i]
		}
		lineY.push(data)
	}

	var myChart1 = echarts.init(document.getElementById('echarts-bar-chart1'));
	var option = {
		backgroundColor: '#02396b',
		tooltip: {
			trigger: 'axis'
		},
		legend: {
			data: charts.names,
			textStyle: {
				fontSize: 12,
				color: '#fefefe',

			},
			top: '15',
			right: '10'
		},
		grid: {
			top: '25%',
			left: '2%',
			right: '2%',
			bottom: '2%',
			containLabel: true
		},
		xAxis: {
			type: 'category',
			boundaryGap: false,
			data: charts.lineX,
			axisLabel: {
				textStyle: {

					color: '#c6effe'
				},

				formatter: function(params) {
					return params.split(' ')[0]
				}
			},

		},
		yAxis: [{
			type: 'value',
			min: 0,
			max: 12000,
			interval: 2000,

			splitLine: {
				lineStyle: {
					show: false,
					type: 'dashed',
					color: "#364b6d"
				},

			},
			axisLine: {
				lineStyle: {
					color: '#c6effe',
				}
			},

		},
			{
				type: 'value',
				min: 0,
				max: 600,
				interval: 100,
				axisLabel: {
					formatter: '{value}'
				},
				splitLine: {
					show: false,
					lineStyle: {
						show: false,
						type: 'dashed'
					},
				},
				axisLine: {
					lineStyle: {
						color: '#c6effe',
					}
				},
			}
		],
		series: lineY,
	}
	myChart1.setOption(option);

	/*****分级统计****/
	var charts = {
		unit: '户数',
		names: ['co浓度', '环境温度'],
		lineX: ['19-09-01', '19-09-02', '19-09-03', '19-09-04', '19-09-05'],
		value: [
			[2000, 5000, 9500, 6000, 3000, 6000, 10000],
			[3000, 5045, 8000, 1920, 9030, 5800]
		]
	}
	var color = ['rgba(8,125,204', 'rgba(140,154,241']
	var lineY = []

	for(var i = 0; i < charts.names.length; i++) {
		var x = i
		if(x > color.length - 1) {
			x = color.length - 1
		}
		var data = {
			name: charts.names[i],
			type: 'line',
			color: color[x] + ')',
			smooth: true,
			areaStyle: {
				normal: {
					color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
						offset: 0,
						yAxisIndex: 0,
						color: color[x] + ', 0.9)'
					}, {
						offset: 0.8,
						yAxisIndex: 1,
						color: color[x] + ', 0.9)'
					}], false),
					shadowColor: 'rgba(0, 0, 0, 0.1)',
					shadowBlur: 10
				}
			},
			symbol: 'circle',
			symbolSize: 5,
			data: charts.value[i]
		}
		lineY.push(data)
	}
	var myChart = echarts.init(document.getElementById('echarts-bar-chart2'));

	var option = {
		backgroundColor: '#563f52',
		tooltip: {
			trigger: 'axis'
		},
		legend: {
			data: charts.names,
			textStyle: {
				fontSize: 12,
				color: '#fefefe',

			},
			top: '15',
			right: '10'
		},
		grid: {
			top: '25%',
			left: '2%',
			right: '2%',
			bottom: '2%',
			containLabel: true
		},
		xAxis: {
			type: 'category',
			boundaryGap: false,
			data: charts.lineX,
			axisLabel: {
				textStyle: {

					color: '#c6effe'
				},

				formatter: function(params) {
					return params.split(' ')[0]
				}
			},

		},
		yAxis: [{
			type: 'value',
			min: 0,
			max: 12000,
			interval: 2000,

			splitLine: {
				lineStyle: {
					show: false,
					type: 'dashed',
					color: "#364b6d"
				},

			},
			axisLine: {
				lineStyle: {
					color: '#c6effe',
				}
			},

		},
			{
				type: 'value',
				min: 0,
				max: 600,
				interval: 100,
				axisLabel: {
					formatter: '{value}'
				},
				splitLine: {
					show: false,
					lineStyle: {
						show: false,
						type: 'dashed'
					},
				},
				axisLine: {
					lineStyle: {
						color: '#c6effe',
					}
				},
			}
		],
		series: lineY,
	}

	myChart.setOption(option);

	window.onresize = function() {
		myChart.resize();
		myChart1.resize(); //若有多个图表变动，可多写
	}

});