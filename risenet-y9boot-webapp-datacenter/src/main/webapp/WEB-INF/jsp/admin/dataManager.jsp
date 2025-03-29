<%@page contentType="text/html;charset=utf-8"%>
<%@ include file="/static/common/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <title>大数据平台</title>
  <link rel="stylesheet" href="${ctx }/static/css/layui.css" id="layui">
  <link rel="stylesheet" href="${ctx }/static/css/default.css" id="theme">
</head>

<body class="layui-layout-body kit-theme-default">
	<div class="layui-layout layui-layout-admin">
  		<div class="layui-header">
      		<div class="layui-logo">
        		<div class="layui-logo-toggle"><i class="layui-icon">&#xe68e;</i></div>
        		<div class="layui-logo-brand">全域数据监控管理</div>
      		</div>
      		<div class="layui-layout-right">
        		<ul class="kit-nav" lay-filter="header_right">
		          	<li class="kit-item">
		          		<a><i class="layui-icon layui-icon-component"></i><span> ${deptName }</span></a>
		          	</li>
		          	<li class="kit-item">
		            	<a><i class="layui-icon layui-icon-username"></i><span> ${loginName }</span></a>
		          	</li>
        		</ul>
      		</div>
    	</div>
      	<div class="layui-fluid">
		    <div class="layui-col-md12">
	      		<div class="layui-col">
                	<div class="layui-card-body">
                		<div class="layui-tab layui-tab-brief">
					  		<ul class="layui-tab-title">
							    <li class="layui-this">数据中心数据监控</li>
							    <li onclick="appRole();">数据查询控制</li>
							    <li onclick="dataManager();">数据管理</li>
							    <li onclick="kafkaInfo();">kafka消费情况</li>
						  	</ul>
						    <div class="layui-tab-content">
							    <div class="layui-tab-item layui-show">
							    	<div class="layui-col-md12">
								      	<div class="layui-row layui-col-space15">
								            <div class="layui-col-md6">
							              		<div class="layui-card">
								                	<div class="layui-card-header">近一周数据变化情况</div>
									                <div class="layui-card-body">
							                   			<div id="weekNode-total" style="height:310px;"></div>
									                </div>
								              	</div>
								            </div>
								            <div class="layui-col-md6">
								              	<div class="layui-card">
								                	<div class="layui-card-header">数据全量统计</div>
								                	<div class="layui-card-body">
								                  		<div id="allNode_base" style="height:310px;"></div>
									                </div>
								              	</div>
							            	</div>
								            <div class="layui-col-md12">
							              		<div class="layui-card">
								                	<div class="layui-card-header">近一个月总数据变化情况</div>
									                <div class="layui-card-body">
								                  		<div id="mountNode-total" style="height:330px;"></div>
									                </div>
								              	</div>
								            </div>
							        	</div>
							    	</div>
							    </div>
							    <div id="appRole" class="layui-tab-item"></div>
							    <div id="dataManager" class="layui-tab-item"></div>
							    <div id="kafkaInfo" class="layui-tab-item"></div>
						  	</div>
					    </div>
                	</div>
		    	</div>
		  	</div>
		</div>
	    <!-- footer -->
	    <footer class="layui-footer" kit-footer="true">
	      	Copyright © 2019 by www.risesoft.com All Rights Reserved.
	    </footer>
  	</div>
  	<script type="text/javascript" src="${ctx }/static/layui/layui.js"></script><!--此处位置不能变，否则不符合layui规范-->
  	<script type="text/javascript" src="${ctx }/static/js/jquery-2.1.0.js"></script>
  	<script type="text/javascript" src="${ctx }/static/js/echarts.min.js"></script>
  	<script type="text/javascript">
	  	layui.use('element', function(){
		  	var $ = layui.jquery,element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
		});
	  	
	 	function appRole(){
			$("#appRole").load("${ctx}/dataCenter/appRolePage");
	 	}
	 	
	 	function dataManager(){
	 		$("#dataManager").load("${ctx}/dataCenter/dataPage");
	 	}
	 	
	 	function kafkaInfo(){
	 		$("#kafkaInfo").load("${ctx}/dataCenter/kafkaPage");
	 	}
  	</script>
  	<script type="text/javascript">
		//基于准备好的dom，初始化echarts实例
		var myChart = echarts.init(document.getElementById('weekNode-total'));
		myChart.showLoading({
			text:"正在加载中......."
		});
		
		$.get("${ctx}/dataCenter/getCharts", function(data) {
			var weeklist = data.weeklist;
			var typeList = data.typeList;
			var dataList = data.listMap;
			
			var datas = [];
	        for (var i = 0; i < dataList.length; i++) {
	            datas.push({
	            	name: dataList[i].name,
	            	type:'line',
	            	data:dataList[i].dataList
	            });
	        }
			
		    // 指定图表的配置项和数据
		    var option = {
	    	    tooltip: {
	    	        trigger: 'axis',
	    	        axisPointer: {
	    	            type: 'shadow'
	    	        }
	    	    },
	    	    legend: {
	    	        data:typeList
	    	    },
	    	    grid: {
	    	        left: '3%',
	    	        right: '4%',
	    	        bottom: '3%',
	    	        containLabel: true
	    	    },
	    	    toolbox:{
					show:false,
					feature: {
						mark: {show:true},
						magicType: {show: true, type: ['line', 'bar']},
						saveAsImage:{show:true}
					}
				},
	    	    xAxis: [
	    	    	{
	    	        	type: 'category',
	    	        	data: weeklist
	    	    	}
	    	    ],
	    	    yAxis: [
	    	    	{
	    	            name: '增量',
	    	            type: 'value',
	    	            axisLabel: {
	    	                formatter: '{value} 条'
	    	            }
	    	        }
	    	    ],
	    	    series: datas
		    };
		    
		    // 使用刚指定的配置项和数据显示图表
		    myChart.setOption(option);
		    myChart.hideLoading(); 
		},'json');
	</script>
  	<script type="text/javascript">
		//基于准备好的dom，初始化echarts实例
		var myChart2 = echarts.init(document.getElementById('mountNode-total'));
		myChart2.showLoading({
			text:"正在加载中......."
		});
		
		$.get("${ctx}/dataCenter/getCharts2", function(data) {
			var baseList = data.baseList;
			var dataList = data.dataList;
			var numList = data.numList;
			
		    // 指定图表的配置项和数据
		    var option = {
	    	    tooltip: {
	    	        trigger: 'axis',
	    	        axisPointer: {
	    	            type: 'shadow'
	    	        }
	    	    },
	    	    legend: {
	    	        data:['总量','增量']
	    	    },
	    	    grid: {
	    	        left: '3%',
	    	        right: '4%',
	    	        bottom: '3%',
	    	        containLabel: true
	    	    },
	    	    toolbox:{
					show:true,
					feature: {
						mark: {show:true},
						magicType: {show: true, type: ['line', 'bar']},
						saveAsImage:{show:true}
					}
				},
	    	    xAxis: [
	    	    	{
	    	        	type: 'category',
	    	        	data: baseList
	    	    	}
	    	    ],
	    	    yAxis: [
	    	    	{
	    	            name: '总量',
	    	            type: 'value',
	    	            axisLabel: {
	    	                formatter: '{value} 条'
	    	            }
	    	        },
	    	        {
	    	            name: '增量',
	    	            type: 'value',
	    	            axisLabel: {
	    	                formatter: '{value} 条'
	    	            }
	    	        }
	    	    ],
	    	    series: [
	    	    	{
	    	            name:'总量',
	    	            type:'line',
	    	            data:numList
	    	        },
	    	        {
	    	            name:'增量',
	    	            type:'line',
	    	            yAxisIndex: 1,
	    	            data:dataList
	    	        }
	    	    ]
		    };
		    
		    // 使用刚指定的配置项和数据显示图表
		    myChart2.setOption(option);
		    myChart2.hideLoading(); 
		},'json');
	</script>
  	<script type="text/javascript">
		//基于准备好的dom，初始化echarts实例
		var myChart3 = echarts.init(document.getElementById('allNode_base'));
		myChart3.showLoading({
			text:"正在加载中......."
		});
		
		$.get("${ctx}/dataCenter/getCharts3", function(data) {
			var typeList = data.typeList;
			var dataList = data.dataList;
			
		    // 指定图表的配置项和数据
		    var option = {
	    		title: {
	    	        text: "总数量："+data.countAll
	    	    },	
	    	    tooltip: {
	    	        trigger: 'axis',
	    	        axisPointer: {
	    	            type: 'shadow'
	    	        }
	    	    },
	    	    legend: {
	    	        data:['总量','增量']
	    	    },
	    	    grid: {
	    	        left: '3%',
	    	        right: '4%',
	    	        bottom: '3%',
	    	        containLabel: true
	    	    },
	    	    toolbox:{
					show:true,
					feature: {
						mark: {show:true},
						magicType: {show: true, type: ['line', 'bar']},
						saveAsImage:{show:true}
					}
				},
	    	    xAxis: [
	    	    	{
	    	        	type: 'category',
	    	        	data: typeList
	    	    	}
	    	    ],
	    	    yAxis: [
	    	    	{
	    	            name: '总量',
	    	            type: 'value',
	    	            boundaryGap: [0, 0.01],
	    	            axisLabel: {
	    	                formatter: '{value} 条'
	    	            }
	    	        }
	    	    ],
	    	    series: [
	    	        {
	    	            name:'总量',
	    	            type:'bar',
	    	            data:dataList
	    	        }
	    	    ]
		    };
		    
		    // 使用刚指定的配置项和数据显示图表
		    myChart3.setOption(option);
		    myChart3.hideLoading(); 
		},'json');
	</script>
</body>
</html>