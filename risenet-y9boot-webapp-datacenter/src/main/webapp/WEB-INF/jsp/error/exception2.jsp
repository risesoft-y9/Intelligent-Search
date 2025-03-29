<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/static/common/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>提示</title>
</head>
<body style="background-color: #ffffff;">
	<div style="height: 100%;width: 100%;">
	    <div style="margin:0 auto;width:750px;height:100%;border: none;background-color:#ffffff;">
	        <img src="${ctx}/static/images/forbidden.jpg"
	             style="display: block;margin: 0 auto 50px auto;width: 450px;height:340px; ;"/>
	        <p style="text-align:center;color: #000000;font-family: 'microsoft yahei';font-size: 40px;font-weight: 500;">
	          	${msg }
	        </p>
	    </div>
	</div>
</body>
</html>
