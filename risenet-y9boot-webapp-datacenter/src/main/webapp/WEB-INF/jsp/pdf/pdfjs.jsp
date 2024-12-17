<%@page contentType="text/html;charset=utf-8"%>
<%@ include file="/static/common/taglib.jsp"%>
<html lang="en" xmlns:th="http://www.w3.org/1999/xhtml">
<head>
	<script type="text/javascript" src="${ctx }/static/js/jquery-3.5.1.min.js"></script>
    <meta charset="UTF-8">
    <title>pdfjs跳转到指定页面并高亮显示关键词</title>
    <th:block th:insert="include::header"></th:block>
</head>
<body>
<h1>pdfjs跳转到指定页面并高亮显示关键词</h1>
<iframe width="100%" height="100%" id="pdf"></iframe>
</body>
<th:block th:insert="include::js"></th:block>
<script type="text/javascript">
var id="${id}";
var fileName="${fileName}";
//高亮显示关键词
var keyword='';
//跳转到指定页面 /pdf/localPdf为控制层获取pdf字节响应给浏览器
//$('#pdf').attr('src',ctx+'/plugin/pdfjs/web/viewer.html?file='+encodeURIComponent(ctx+'/pdf/localPdf')+'#page='+page);
//传入关键字
$('#pdf').attr('src','${ctx}/static/pdfjs/web/viewer.html?file='+encodeURIComponent('${ctx}/pdf/localPdf?id='+encodeURI(id)+'&fileName='+encodeURI(fileName))+'&keyword='+keyword);
</script>
</html>