<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/static/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
    <link rel="stylesheet" href="${ctx }/static/layui/css/layui.css">
    <script src="${ctx}/static/js/jquery-1.10.2.min.js"></script>
	<script src="${ctx }/static/layui/layui.js" charset="utf-8"></script>
	<script type="text/javascript" src="${ctx }/static/layer/layer.js"></script>
	<style type="text/css">
		table{
			border-collapse:collapse;
		}
		table,th, td{
			border: 1px solid #999;
		}
		th,td{
			height: 28px;
		}
    </style>
</head>
<body>
<div>
    <table style="width: 99%;margin: 5px;">
		<thead>
			<tr>
				<th style="color: #999;width: 5%;">类别</th>
				<th style="color: #999;width: 15%;">流程ID</th>
				<th style="color: #999;width: 25%;">名字</th>
				<th style="color: #999;width: 45%;">内容</th>
				<th style="color: #999;width: 5%;">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${not empty hlist }">
				<c:forEach items="${hlist }" var="list">
					<tr>
						<td align="center">历程</td>
						<td align="center">${list.processInstanceId }</td>
						<td align="center">${list.assignee }</td>
						<td align="center">${list.opinionContent }</td>
						<td align="center">
							<a href="javascript:void(0);" onclick="delInfo('${list.id}','banjian2');"><span style="color: blue;">删除</span></a>
						</td>
					<tr>
				</c:forEach>
			</c:if>
			<c:if test="${not empty elist }">
				<c:forEach items="${elist }" var="list">
					<tr>
						<td align="center">表单</td>
						<td align="center">${list.processInstanceId }</td>
						<td align="center">${list.eformName }</td>
						<td align="center">${list.fieldValues }</td>
						<td align="center">
							<a href="javascript:void(0);" onclick="delInfo('${list.id}','banjian3');"><span style="color: blue;">删除</span></a>
						</td>
					<tr>
				</c:forEach>
			</c:if>
		</tbody>
	</table>
</div>
	<script type="text/javascript">
		function delInfo(id,type){
			layer.confirm('确定要删除该信息吗？', {
			    btn: ['确定','取消'],  //按钮
			    closeBtn: false,
			    shadeClose: true, //点击遮罩关闭层
			}, function(){
				$.ajax({
					type: 'POST',
					dataType: 'json',
					data: {
						id: id,
						type: type
					},
					url: '${ctx}/dataCenter/deleteInfo',
					success: function(res) {
						if (res.success) {
							window.location.reload();
						}else{
							alert(res.msg);
						}
					}
				});
			}, function(){
				layer.closeAll();
			});
		}
	</script>
</body>
</html>