<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/static/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>北京市水务局综合信息平台</title>
	<link rel="stylesheet" media="screen and (min-width:0px) and (max-width:1400px)" type="text/css" href="${ctx}/static/datacenter/css/contentdisplay_1366.css">
	<link rel="stylesheet" media="screen and (min-width:1400px) and (max-width:1640px)" type="text/css" href="${ctx}/static/datacenter/css/contentdisplay_1600.css">
	<link rel="stylesheet" media="screen and (min-width:1640px) and (max-width:3000px)" type="text/css" href="${ctx}/static/datacenter/css/contentdisplay_1920.css">
	<script type="text/javascript" src="${ctx }/static/js/jquery-3.5.1.min.js"></script>
</head>
<body style="background-color: white;">
	<div style="position: fixed;top:0;left:0;z-index: 1000;width: 100%;">
		<div class="header">
		    <img src="${ctx }/static/img/logo.png" style="height: 45px;padding: 5px;">
		    <div class="personInfo">
				<!-- <a>所属部门：办公室</a>
				<a>当前用户：王副主任</a> -->
			</div>
		</div>
	</div>
	<div class="title">
	    <div class="title_name">
	        ${info.biaoti }
	    </div>
	</div>
	<div class="db_info">
	    <div class="db_info_content">
	        <span>文号：${info.wenhao }</span>
	        <span>缓急：${info.huanji }</span>
	        <span>密级：${info.chayue }</span>
	        <span>类型：${info.appName }</span>
	        <span>日期：${info.createTime }</span>
	        <span>浏览次数：${info.clickNum }</span>
	    </div>
	</div>
	<div class="db_main" id="db_main">
	    <div class="db_main_left" id="db_main_left">
	        <div class="db_txt" id="db_txt">
		        <div class="para-title level-2">
		        	<h2 class="title-text">基本信息</h2>
		        </div>
				<table cellpadding="0" cellspacing="0" style="border: 0px solid;margin-bottom: 10px;width: 100%;">
					<tbody>
						<tr>
							<td align="center" height="35" width="150" style="color: #999">报送部门</td>
							<td align="left" width="400">${info.baosongbumen }</td>
							<td align="center" height="35" width="150" style="color: #999">报送人</td>
							<td align="left" width="400">${info.baosongren }</td>
						</tr>
						<tr>
							<td align="center" height="35" width="150" style="color: #999">报送日期</td>
							<td align="left" width="400">${info.baosongriqi }</td>
							<td align="center" height="35" width="150" style="color: #999">备&nbsp;&nbsp;注</td>
							<td align="left" width="400">${info.beizhu }</td>
						</tr>
						<tr>
							<td align="center" height="35" width="150" style="color: #999">摘要</td>
							<td align="left" colspan="3" class="context">${info.documentContent }</td>
						</tr>
					</tbody>
				</table>
				<div class="para-title level-2">
		        	<h2 class="title-text">附件信息</h2>
		        </div>
		        <div class="annex">
			        <ul>
			        	<c:if test="${empty info.attachments && empty info.documentUrl }">
							<li style="color: #999;text-align: center;">没有相关数据</li>
						</c:if>
						<c:if test="${not empty info.documentUrl }">
							<li>
				                <img src="${ctx}/static/datacenter/img/paperclip.png">
				                <a href="javascript:void(0);" onclick="downFile('${info.documentUrl}','[正文]${info.biaoti}.doc');" title="点击下载正文">正文.doc</a>
				            </li>
						</c:if>
						<c:if test="${not empty info.attachments }">
				        	<c:forEach items="${info.attachments }" var="file" varStatus="status">
				        		<li>
					                <img src="${ctx}/static/datacenter/img/paperclip.png">
					                <a href="javascript:void(0);" onclick="downFile('${file.fileUrl}','${file.fileName }');" title="点击下载附件">${file.fileName }</a>
					            </li>
				        	</c:forEach>
			        	</c:if> 
			        </ul>
			    </div>
				<div class="para-title level-2">
		        	<h2 class="title-text">历程信息</h2>
		        </div>
		        <table class="table-history" style="width: 100%;">
					<thead>
						<tr>
							<th style="color: #999" width="20">序号</th>
							<th style="color: #999" width="100">环节</th>
							<th style="color: #999" width="100">办理人</th>
							<th style="color: #999" width="100">办理时间</th>
							<th style="color: #999" width="250">办理内容</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${empty info.comments }">
							<tr><td colspan="5" align="center">没有相关数据</td></tr>
						</c:if>
						<c:if test="${not empty info.comments }">
							<c:forEach items="${info.comments }" var="list" varStatus="statu">
								<tr>
									<td align="center">${statu.count }</td>
									<td align="center">${list.comment }</td>
									<td align="center">${list.commentPerson }</td>
									<td align="center">${list.commentDate }</td>
									<td align="center">${list.content }</td>
								<tr>
							</c:forEach>
						</c:if>
					</tbody>
				</table>
	        </div>
	    </div>
	    <div class="db_main_right">
	        <div class="db_related">
	            <div class="db_related_title">
	                <span>相关数据</span>
	            </div>
	            <div class="db_related_content">
	                <ul>
	                    <li>
	                        <a>
	                            <span class="related_span"></span>
	                            <span class="related_span_1"></span>
	                        </a>
	                    </li>
	                </ul>
	            </div>
	        </div>
	    </div>
	</div>
</body>
<script type="text/javascript">
	var title = "${info.biaoti}";
	
	function downFile(fileUrl, fileName){
        var url = "${ctx}/officeInfo/download?name="+encodeURI(fileName)+"&url="+encodeURIComponent(fileUrl);
        //window.location.href = url;
        window.open(url);
 	}
	
	$(document).ready(function(){
	 	$.ajax({//获取相关数据
			type : 'GET',
			url:'${ctx}/officeInfo/searchTitleResult?title='+encodeURI(title)+'&page=1&rows=10',
			dataType:'JSON',
			success : function(data, status){
				if(data.success){
					var rows = data.rows;
					if(rows.length > 1){//rows有一条为当前数据
		        		var str = "";
			        	$.each(rows,function(i,item){
				        	if(i > 0)
			        			str +='<li><a href="javascript:void(0);" onclick=openDoc("'+item.id+'","'+item.dataType+'")><span class="related_span">['+item.dataType+']'+item.title+'</span></a></li>';
						});
			        	$(".db_related_content").html(str);
		        	}else{
		        		$(".db_related_content").html("<span style='font-size: 12px;margin: 10px 5px 10px 25px;position: absolute;'>无相关数据</span>");
		        	}
				}
			}
	 	});
	});

	function openDoc(id, dataType){
    	window.open('${ctx}/officeInfo/textdisplay?id='+encodeURI(id)+'&dataType='+encodeURI(dataType));
    } 
	
	window.onload = function(){
    	var ininWordArr = [];//用来存储所有段落的文字
        var nowWordArr = [];//用来存储隐藏后所有段落的文字
        var wordTest=$(".context").text();
        var initWordLength = wordTest.length;//当前段落文字的长度
        if(initWordLength > 1200){
        	ininWordArr.push(wordTest.substr(0,1200));
        }else{
        	ininWordArr.push(wordTest);
        }
        var nowWord;
        if (initWordLength > 620) {
            nowWord = wordTest.substr(0,620)+'....<span class="btnShow" style="color:blue;">[展开]</span>';
        } else {
            nowWord = wordTest;
        }
        $(".context").html(nowWord);
        nowWordArr.push(nowWord);
        
        $(document).on('click','.btnShow',function(){
            var i = $('.btnShow').index($(this));
            if ($(this).html() == '[展开]') {
                $(".context").html(ininWordArr[i]+'<span class="btnShow" style="color:blue;">[收起]</span>');
            } else {
                $(".context").html(nowWordArr[i]);
            }
            return false;
        })
    }
    
</script>
</html>