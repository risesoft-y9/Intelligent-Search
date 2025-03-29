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
  <link rel="stylesheet" href="${ctx}/static/dataCenter/css/jquery.bigautocomplete.css">
</head>

<body class="layui-layout-body kit-theme-default search">
	<div class="layui-layout layui-layout-admin">
		<!--头部-->
		<div class="layui-header">
			<div class="layui-logo" style="width: 400px;">
				<div class="layui-logo-toggle" kit-toggle="side">
					<i class="layui-icon">&#xe65b;</i>
				</div>
				<div class="layui-logo-brand">
					<c:choose>
						 <c:when test="${aliasName!='' && aliasName!=null }">
							${aliasName}-数据中心
						</c:when>
						<c:otherwise> 
							${tenantName}-数据中心
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="layui-layout-right">
				<ul class="kit-nav" lay-filter="header_right">
					<c:if test="${role==1 }">
						<li class="kit-item">
							<a href="${ctx}/dataCenter/dataManager" target="_blank"><i class="layui-icon layui-icon-set"></i><span> 后台管理</span></a>
						</li>
					</c:if>
					<li class="kit-item">
						<a><i class="layui-icon layui-icon-username"></i><span> ${deptName }</span></a>
					</li>
					<li class="kit-item">
						<a><span>当前用户： ${loginName }</span></a>
					</li>
					<li class="kit-item">
						<a id="logout"><span>返回首页</span></a>
					</li>
				</ul>
			</div>
		</div>
  		<!--body部分-->
		<div class="layui-body layui-main search-body" kit-body="true">
			<div class="default-search-main">
				<div class="default-search-logo">
					<span>全域大数据中心</span>
				</div>
				<div class="layui-form-item" style="margin-bottom: 35px;">
					<div class="layui-input-block layui-inline default-search-content">
						<input type="text" id="searchbox" placeholder="组合查询方法：a b或者a+b" autofocus="autofocus" autocomplete="off" class="layui-input default-search-con-input">
					</div>	
					<div style="float:left;">
						<button id="searchbtn" type="button" class="layui-btn layui-inline layui-bg-orange default-search-con-btn" >
							<i class="layui-icon layui-icon-search"></i>
						</button>
					</div>
				</div>
				<div class="layui-row default-search-keywords">
					<div class="search-keywords">
						<a>热词推荐&nbsp;：</a>
						<span class="layui-breadcrumb" lay-separator="|">
							<c:forEach items="${listMap }" var="list">
								<a href="javascript:void(0);" title="${list.name }" onclick="goToSearchResult('${list.name}');">${list.spec }</a>
							</c:forEach>
         				</span><br/><br/>
						<a>数据分类&nbsp;：</a>
						<span class="layui-breadcrumb" lay-separator="|">
		  					<a>办件</a>
							<a>报销</a>
							<a>工单</a>
							<a>收文发文</a>
							<a>对外合作</a>
							<a>部门号</a>
							<a>cms</a>
         				</span>
					</div>					
				</div>
			</div>
			<!-- <footer class="layui-footer default-search-footer" kit-footer="true">
				Copyright © 2019 by www.xizhiwuliang.com All Rights Reserved.
			</footer> -->
		</div>
	</div>
  	<script type="text/javascript" src="${ctx }/static/layui/layui.js"></script><!--此处位置不能变，否则不符合layui规范-->
  	<script type="text/javascript" src="${ctx }/static/js/jquery-2.1.0.js"></script>
  	<script type="text/javascript" src="${ctx}/static/dataCenter/js/jquery.bigautocomplete.js" charset="UTF-8"></script>
  	<script type="text/javascript">
	  	layui.use(['laypage', 'layer', 'element'], function() {
			var laypage = layui.laypage,
				layer = layui.layer,
				element = layui.element;
			//自定义每页条数的选择项
			laypage.render({
				elem: 'demo11',
				count: 1000,
				limit: 100,
				limits: [100, 300, 500]
			});
		})
		
		$(document).ready(function(){
			//返回首页
			$("#logout").click(function logout() {
				location.href = "${homeBaseURL}";
			});
			
			//回车搜索
			$('#searchbox').bind('keypress',function(event){
			    if(event.keyCode==13) {
			    	search();
			    }
			});
			
			//按钮搜索
			$("#searchbtn").click(function() {
				search();
			});
			
			$("#searchbox").bigAutocomplete({
	            url : '${ctx}/officeInfo/queryList',
	            callback:function(data){
	                window.location.href='${ctx}/officeInfo/goToSearchResult?searchName='+encodeURI(data.title);
	            }
	        });
		});
	  	
		function search() {
			var searchName = $("#searchbox").val();
	        if (searchName == ""||searchName.replace(/(^\s*)|(\s*$)/g, "")=="") {
	            window.location.reload();
	        }else{
	        	window.location.href='${ctx}/officeInfo/goToSearchResult?searchName='+encodeURI(searchName);
			}
		}
	  	
	  	//点击热词推荐搜索
	  	function goToSearchResult(name){
	  		window.location.href='${ctx}/officeInfo/goToSearchResult?searchName='+encodeURI(name);
	  	}
  	</script>
</body>
</html>