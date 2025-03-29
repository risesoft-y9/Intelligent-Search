<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/static/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<link rel="stylesheet" type="text/css"  href="${ctx}/static/dataCenter/css/jquery.bigautocomplete.css">
	<link rel="stylesheet"  media="screen and (min-width:0px) and (max-width:1400px)" type="text/css" href="${ctx }/static/dataCenter/css/searchResult_1366.css" />
	<link rel="stylesheet"  media="screen and (min-width:1400px) and (max-width:1640px)" type="text/css" href="${ctx }/static/dataCenter/css/searchResult_1600.css" />
	<link rel="stylesheet"  media="screen and (min-width:1640px) and (max-width:3000px)" type="text/css" href="${ctx }/static/dataCenter/css/searchResult_1920.css" />
	
	<script type="text/javascript" src="${ctx}/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/window.js"></script>
	<script type="text/javascript" src="${ctx}/static/dataCenter/js/jquery.bigautocomplete.js"></script>
	<script type="text/javascript" src="${ctx}/static/layer/layer.js"></script>
    <title>${searchName}_数据中心</title>
</head>
<body style="background-color: white;">
<div style="position: fixed;top:0;left:0;z-index: 1000;width: 100%">
    <div class="top">
        <div style="position: absolute;margin-top: 14px;margin-left: 25px;width: 400px;display: inline;color: #FFFFFF;">
            <c:choose>
			 	<c:when test="${aliasName!='' && aliasName!=null }">
					${aliasName} | 搜索
				</c:when>
				<c:otherwise> 
					${tenantName} | 搜索
				</c:otherwise>
			</c:choose>
        </div>
        <div class="personInfo" style="min-width: 500px;margin-top: 14px;">
			<a id="logout" style="border-right: 0px solid #ffffff;">返回首页</a>
			<a>所属部门：${deptName }</a>
			<a>当前用户：${loginName }</a>
		</div>
    </div>
    <div class="search">
        <div class="left">
            <a href="${ctx}/" style="text-decoration: none">数据中心</a>
        </div>
        <div class="right">
            <input id="searchbox" class="searchbox" maxlength="50" value="${searchName }" type="text">                 
             <button id="searchbtn" class="searchbtn">搜索</button> 
        </div>
    </div>
</div>
<div class="wrap">
    <div class="wrap wrapper03" id="wrap03">
        <ul id="wrapUl">
            
        </ul>
    </div>
</div>
<div class="totalResult">
	<div class="dorp">
		<span>时间过滤</span>
		<ul class="dorp_con">
			<li style="line-height: 30px;" onclick="search2('');"><a href="javascript:void(0);" style="text-decoration: none;color: #999;">时间不限</a></li>
			<li style="line-height: 30px;" onclick="search2('今天内');"><a href="javascript:void(0);" style="text-decoration: none;color: #999;">今天内</a></li>
	    	<li style="line-height: 30px;" onclick="search2('一周内');"><a href="javascript:void(0);" style="text-decoration: none;color: #999;">一周内</a></li>
	    	<li style="line-height: 30px;" onclick="search2('一月内');"><a href="javascript:void(0);" style="text-decoration: none;color: #999;">一月内</a></li>
	  	</ul>
  	</div>
    <span class="nums">找到3391条结果(用时0.718秒)</span>
</div>
<div class="search-result" id="search-result">
	<div style="margin-top:-8px;margin-bottom:10px">
		<span class="notice">
			<img class="ic-icon" src="${ctx}/static/dataCenter/img/warnning.png" style="height: 16px;width: 16px"></img>
			<strong></strong>
		</span>
	</div>
	<div id="result">
	   
    </div>
    <div id="pager2" class="pager clearfix"></div>
</div>
<div id="right_content" class="right_content">
	<div class="cr-title c-gap-bottom opr-toplist-title" title="搜索热点">搜索热点</div>
	<table style="width: 100%" class="right-table">
		<thead style="background-color: #fafafa;">
			<th class="right-table left-th">排名</th>
			<th class="right-table right-th">搜索指数</th>
		</thead>
		<tbody id="searchHotspots">
			
		</tbody>
	</table>
</div>
</body>
<script type="text/javascript" src="${ctx}/static/dataCenter/js/jquery.z-pager.js" charset="utf-8"></script>
<script type="text/javascript">
	var searchName;
	var searchTime = "";
	$(document).ready(function(){
		 searchName = $("#searchbox").val();
		 var showName = "${showName}";
         if(showName!=""){
            $(".notice").css("display","block");
            $(".notice").html("<img class='ic-icon' src='${ctx}/static/dataCenter/img/warnning.png'' style='height: 16px;width: 16px'></img><strong>“"+showName+ "”及其后面的字词均被忽略,查询限制在38个汉字以内.");
         }
		 setTimeout("search('"+searchName+"','')",10);//延迟执行，避免第一页不高亮显示
		 getHotword();
	}); 
	
	//返回首页
	$("#logout").click(function logout() {
		location.href = "${ctx }/" ;
	}); 
	
	//获取搜索热点
	function getHotword(){
		 var time = new Date().getTime();
		 $.ajax({
			type : 'GET',
			url:'${ctx}/officeInfo/getHotword?time='+time,
			dataType:'JSON',
			success : function(data, status){
				if(data.success){
					var rows = data.result;
					if(rows.length > 0){//rows有一条为当前数据
						var str = "";
			        	$.each(rows,function(i,item){
			        		var text = item.name;
			        		if(text.length>12){
			        			text = text.slice(0,12)+"…";
			        		}
			        		if(item.index < 4){
			        			str +='<tr>'
										+'<td class="left-td">'
											+'<span class="c-index c-index-hot'+item.index+' c-gap-icon-right-small">'+item.index+'</span>'
											+'<a id="hotword'+item.index+'" href="javascript:void(0);" title="'+item.name+'" onclick=goToSearch("'+item.index+'")>'+text+'</a>'
										+'</td>'
										+'<td class="right-td">'+item.count+'</td>'
									+'</tr>';
			        		}else{
			        			str +='<tr>'
										+'<td class="left-td">'
											+'<span class="c-index c-gap-icon-right-small">'+item.index+'</span>'
											+'<a id="hotword'+item.index+'" href="javascript:void(0);" title="'+item.name+'" onclick=goToSearch("'+item.index+'")>'+text+'</a>'
										+'</td>'
										+'<td class="right-td">'+item.count+'</td>'
									+'</tr>';
			        		}
						});
			        	$("#searchHotspots").html(str);
		        	}
				}
			}
		});
	}
	
	function search2(timeType){
		search(searchName,timeType);
	}
	
	function search(searchName,timeType){
	 	$("#pager2").zPager({
	        url:'${ctx}/officeInfo/searchOfficeInfo?searchName='+encodeURI(searchName)+'&timeType='+encodeURI(timeType)+'&rows=10',
	        htmlBox: $('#search-result'),
			pageData: 10, //每页数据条数
			current: 1, //当前页码数
			minPage: 10, //最小页码数，页码小于此数值则不显示上下分页按钮
			btnBool: true, //是否显示上一页下一页
			btnShow: true, //是否显示第一页和最后一页按钮
	        ajaxSetData: true,//是否使用ajax获取数据 此属性为真时需要url和htmlBox不为空
	        dataRender: function(total,data,result,time){
	        	if(data.length <= 0){
	        		$(".nums").html("抱歉，没有找到相关的内容");
	        		$("#pager2").hide();//没有数据隐藏分页
	        	}else{
	        		if(time!="" && time!=null){
	        			searchTime = time;
	        		}
	        		$(".nums").html("为您找到"+timeType+total+"条相关结果(用时"+searchTime+"秒)");
	        		$("#pager2").show();
	        	}
	        	var html = "";
	        	$.each(data,function(i,item){//搜索结果处理
	        		if(item.dataType=='收文'){
	        			html += '<dl>'
		        			+'<dt class="mb6 clearfix">'
		        			+'<p class="fl">'
			        			+'<span name="highlight">【'+item.dataType+'】</span>'
			        			+'<a name="highlight" style="font-weight: 700;" href="javascript:void(0);" onclick=openDoc("'+item.id+'")>'+item.title+'</a>'
		        			+'</p>'
		        			+'</dt>'
		        			+'<dd class="clearfix">'
			        			+'<div class="summary-box fl">'
				        			+'<p name="highlight" class="summary">'+item.text+'</p>'
				        			+'<div class="detail lh21">'
					        			+'<span>部门：<span name="highlight">'+item.wenhao+'</span></span>'
					        			+'<span>创建者：<span name="highlight">'+item.creatUserName+'</span></span>'
					        			+'<span>创建时间：'+item.startTime+'</span>'
					        			+'<span>浏览次数：'+item.clickNum+'次</span>'
					        			+'<span>匹配度：'+item.score+'</span>'
				        			+'</div>'
			        			+'</div>'
		        			+'</dd>'
        				+'</dl>';
	        		}else if(item.dataType=='news'){
	        			html += '<dl>'
		        			+'<dt class="mb6 clearfix">'
		        			+'<p class="fl">'
			        			+'<span name="highlight">【'+item.appCNName+'】</span>'
			        			+'<a name="highlight" style="font-weight: 700;" href="javascript:void(0);" onclick=openDoc2("'+item.url+'","'+item.id+'")>'+item.title+'</a>'
		        			+'</p>'
		        			+'</dt>'
		        			+'<dd class="clearfix">'
			        			+'<div class="summary-box fl">'
				        			+'<p name="highlight" class="summary">'+item.zy+'</p>'
				        			+'<div class="detail lh21">'
					        			+'<span>创建者：<span name="highlight">'+item.creatUserName+'</span></span>'
					        			+'<span>创建时间：'+item.startTime+'</span>'
					        			+'<span>浏览次数：'+item.clickNum+'次</span>'
					        			+'<span>匹配度：'+item.score+'</span>'
				        			+'</div>'
			        			+'</div>'
		        			+'</dd>'
        				+'</dl>';
	        		}else if(item.dataType=='cms'){
	        			var itemzy=item.zy;
	        			if(itemzy==null){
	        				itemzy="";
	        			}
	        			html += '<dl>'
		        			+'<dt class="mb6 clearfix">'
		        			+'<p class="fl">'
			        			+'<span name="highlight">【'+item.appCNName+'】</span>'
			        			+'<a name="highlight" style="font-weight: 700;" href="javascript:void(0);" onclick=openDoc3("'+item.url+'","'+item.id+'")>'+item.title+'</a>'
		        			+'</p>'
		        			+'</dt>'
		        			+'<dd class="clearfix">'
			        			+'<div class="summary-box fl">'
				        			+'<p name="highlight" class="summary">'+itemzy+'</p>'
				        			+'<div class="detail lh21">'
					        			+'<span>创建者：<span name="highlight">'+item.creatDeptName+'</span></span>'
					        			+'<span>创建时间：'+item.startTime+'</span>'
					        			+'<span>浏览次数：'+item.clickNum+'次</span>'
					        			+'<span>匹配度：'+item.score+'</span>'
				        			+'</div>'
			        			+'</div>'
		        			+'</dd>'
        				+'</dl>';
	        		}
	        	});
	        	$('#result').html(html);
	        	typeCount(timeType);
	        }
	    });
	 }
	
	//按类型搜索统计
	function typeCount(timeType){
		$.ajax({
			type : 'GET',
			url : '${ctx}/officeInfo/typeCount?searchName='+encodeURI(searchName)+'&timeType='+encodeURI(timeType),
			dataType:'JSON',
			success : function(data, status){
				if(data.success){
					var result = data.result;
					var str = "";
					if(result.length > 0){
			        	$.each(result,function(i,item){//各应用搜索统计处理
			        		str += '<li><a href="javascript:void(0);" name="'+item.name+'" onclick=showTypeResult("'+item.name+'","'+timeType+'"); style=" text-decoration:none">'+item.name+'('+item.count+')</a></li>';
						});
		        	}
					$("#wrapUl").html(str);
				}
			}
		});
	}
	
	//搜索跳转结果
	function goToSearchResult(searchName){
		var time = new Date().getTime();
		window.location.href='${ctx}/officeInfo/goToSearchResult?searchName='+encodeURI(searchName)+'&time='+time;
	}

	function goToSearch(num){
		var id = "#hotword" + num;
		var searchName = $(id).attr('title');
		var time = new Date().getTime();
		window.location.href='${ctx}/officeInfo/goToSearchResult?searchName='+encodeURI(searchName)+'&time='+time;
	}
	
	//搜索重新加载页面
    $("#searchbtn").click(function searchReload() {
	    searchName = $("#searchbox").val();
		if(searchName.length==0){//搜索为空，页面重定位到主页面
			window.location.href='${ctx}';
		}else{
			goToSearchResult(searchName);
		}
    });
	
  	//回车搜索
	$('#searchbox').bind('keypress',function(event){ 
	    if(event.keyCode==13) {
	    	 searchName = $("#searchbox").val();
	    	 if(searchName.length==0){//搜索为空，页面重定位到主页面
	 			window.location.href='${ctx}';
	 		 }else{
	 			goToSearchResult(searchName);
			 }
	    }
	});
    
    //按类型搜索 
    function showTypeResult(appName,timeType){
    	 $("#pager2").zPager({
 	        url:'${ctx}/officeInfo/searchTypeResult?appName='+encodeURI(appName)+'&searchName='+encodeURI(searchName)+'&timeType='+encodeURI(timeType)+'&rows=10',
 	        htmlBox: $('#search-result'),
 			pageData: 10, //每页数据条数
 			current: 1, //当前页码数
 			minPage: 10, //最小页码数，页码小于此数值则不显示上下分页按钮
 			btnBool: true, //是否显示上一页下一页
 			btnShow: true, //是否显示第一页和最后一页按钮
 	        ajaxSetData: true,//是否使用ajax获取数据 此属性为真时需要url和htmlBox不为空
 	        dataRender: function(total,data,result){
 	        	var html = "";
 	        	$.each(data,function(i,item){//搜索结果处理
 	        		if(item.dataType=='banjian'){
	        			html += '<dl>'
		        			+'<dt class="mb6 clearfix">'
		        			+'<p class="fl">'
			        			+'<span name="highlight">【'+item.appCNName+'】</span>'
			        			+'<a name="highlight" style="font-weight: 700;" href="javascript:void(0);" onclick=openDoc("'+item.id+'")>'+item.title+'</a>'
		        			+'</p>'
		        			+'</dt>'
		        			+'<dd class="clearfix">'
			        			+'<div class="summary-box fl">'
				        			+'<p name="highlight" class="summary">'+item.text+'</p>'
				        			+'<div class="detail lh21">'
					        			+'<span>部门：<span name="highlight">'+item.creatDeptName+'</span></span>'
					        			+'<span>创建者：<span name="highlight">'+item.creatUserName+'</span></span>'
					        			+'<span>创建时间：'+item.startTime+'</span>'
					        			+'<span>浏览次数：'+item.clickNum+'次</span>'
				        			+'</div>'
			        			+'</div>'
		        			+'</dd>'
        				+'</dl>';
	        		}else if(item.dataType=='news'){
	        			html += '<dl>'
		        			+'<dt class="mb6 clearfix">'
		        			+'<p class="fl">'
			        			+'<span name="highlight">【'+item.appCNName+'】</span>'
			        			+'<a name="highlight" style="font-weight: 700;" href="javascript:void(0);" onclick=openDoc2("'+item.url+'","'+item.id+'")>'+item.title+'</a>'
		        			+'</p>'
		        			+'</dt>'
		        			+'<dd class="clearfix">'
			        			+'<div class="summary-box fl">'
				        			+'<p name="highlight" class="summary">'+item.zy+'</p>'
				        			+'<div class="detail lh21">'
					        			+'<span>创建者：<span name="highlight">'+item.creatUserName+'</span></span>'
					        			+'<span>创建时间：'+item.startTime+'</span>'
					        			+'<span>浏览次数：'+item.clickNum+'次</span>'
				        			+'</div>'
			        			+'</div>'
		        			+'</dd>'
        				+'</dl>';
	        		}else if(item.dataType=='cms'){
	        			html += '<dl>'
		        			+'<dt class="mb6 clearfix">'
		        			+'<p class="fl">'
			        			+'<span name="highlight">【'+item.appCNName+'】</span>'
			        			+'<a name="highlight" style="font-weight: 700;" href="javascript:void(0);" onclick=openDoc3("'+item.url+'","'+item.id+'")>'+item.title+'</a>'
		        			+'</p>'
		        			+'</dt>'
		        			+'<dd class="clearfix">'
			        			+'<div class="summary-box fl">'
				        			+'<p name="highlight" class="summary">'+item.zy+'</p>'
				        			+'<div class="detail lh21">'
					        			+'<span>创建者：<span name="highlight">'+item.creatDeptName+'</span></span>'
					        			+'<span>创建时间：'+item.startTime+'</span>'
					        			+'<span>浏览次数：'+item.clickNum+'次</span>'
				        			+'</div>'
			        			+'</div>'
		        			+'</dd>'
        				+'</dl>';
	        		}
 	        	});
 	        	$('#result').html(html);
 	        }
 	    }); 
    }
    
    //打开文档
    function openDoc(id){
    	//layer弹出一个iframe层
    	layer.closeAll();
        var index = layer.open({
          type: 2,
          shadeClose: true, //点击遮罩关闭层
          area: ['90%', '80%'],
          content: '${ctx}/officeInfo/textdisplay?id='+id+'&searchName='+encodeURI(searchName),
          maxmin: true
        });
        layer.full(index);
    }
    
    function openDoc2(url,id){
    	$.ajax({
			type : 'GET',
			url : '${ctx}/officeInfo/saveClick?id='+id,
			dataType:'JSON',
			success : function(data){},
	 	});
    	window.open(url);
    }
    
    function openDoc3(url,id){
    	$.ajax({
			type : 'GET',
			url : '${ctx}/officeInfo/saveClick2?id='+id,
			dataType:'JSON',
			success : function(data){},
	 	});
    	window.open(url);
    }
    
    $(function(){
        $("#searchbox").bigAutocomplete({
            url : '${ctx}/officeInfo/queryList',
            callback:function(data){
                window.location.href='${ctx}/officeInfo/goToSearchResult?searchName='+encodeURI(data.title);
            }
        });
    });
</script>
</html>