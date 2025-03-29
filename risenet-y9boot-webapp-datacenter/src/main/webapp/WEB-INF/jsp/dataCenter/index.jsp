<%@page contentType="text/html;charset=utf-8"%>
<%@ include file="/static/common/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<title>北京市水务局综合信息平台</title>
	<link rel="stylesheet" href="${ctx }/static/css/reset.css" />
	<link rel="stylesheet" href="${ctx }/static/css/layui.css" />
	<link rel="stylesheet" href="${ctx }/static/css/index.css" />
	<link rel="stylesheet" href="${ctx }/static/css/jquery-labelauty.css">
	<link rel="stylesheet" href="${ctx}/static/datacenter/css/jquery.bigautocomplete.css">
	<script type="text/javascript" src="${ctx }/static/js/jquery-3.5.1.min.js"></script>
	<script type="text/javascript" src="${ctx }/static/js/jquery-labelauty.js"></script>
	<script type="text/javascript" src="${ctx }/static/layui.js"></script>
	<script type="text/javascript" src="${ctx}/static/datacenter/jquery.bigautocomplete.js" charset="UTF-8"></script>
</head>
<body>
	<div class="cover"></div>
	<!-- <div class="nav">
		<span class="layui-breadcrumb" lay-separator=" ">
			<a href="">首页</a>
			<a href="">待办</a>
			<a href="">收文</a>
			<a href="">发文</a>
		</span>
		<span class="layui-breadcrumb" lay-separator="|">
			<a href=""></a>
		    <a href="">王副主任</a>
		    <a href="">办公室</a>
		</span>
	</div> -->
	<div class="logo" style="z-index: 99999;">
		<img src="${ctx }/static/img/logo.png">
	</div>
	<div class="search" style="z-index: 99999;">
		<div id="tab_switch">
			<!-- <div id="tab_jz" class="tab_selected">精准版</div> -->
			<!-- <div id="tab_zn" class="tab_unselected">智能版</div> -->
		</div>
		<div class="search_form">
			<div class="search_bar">
				<input type="text" id="search" autocomplete="off" style="border-top-left-radius: 5px;" >
			</div>
			<div class="search_btn" id="search_btn">
				<img src="${ctx }/static/img/search.png" />
			</div>
		</div>
	</div>
	<ul class="search_index" style="z-index: 99999;">
		<!-- <li style="margin-right: 8px;"><input id="all" type="checkbox" name="all" checked data-labelauty="默认全选"></li>
		<li><input type="checkbox" name="checkbox" value="y9_fawen" checked data-labelauty="发文"></li>
		<li><input type="checkbox" name="checkbox" value="y9_shouwen" checked data-labelauty="收文"></li> -->
	</ul>
	<div class="search_index" style="top: 365px;text-indent: 8px;z-index: 99999;" >
		<span class="layui-badge-dot" style="background-color: #fff"></span>
		<span style="color: #fff;font-size: 16px;">当前收录数据范围为2021年1月1日至2022年11月30日已办结的收文、发文(便函、局发文)</span>
	</div>
	<div class="layui-carousel" id="carousel" >
	    <div carousel-item >
		    <div class="bg"><img class="bg_img" src="${ctx }/static/img/bg1.jpg" /></div>
		    <div class="bg"><img class="bg_img" src="${ctx }/static/img/bg2.jpg" /></div>
		    <div class="bg"><img class="bg_img" src="${ctx }/static/img/bg3.jpg" /></div>
		    <div class="bg"><img class="bg_img" src="${ctx }/static/img/bg4.jpg" /></div>
		    <div class="bg"><img class="bg_img" src="${ctx }/static/img/bg5.jpg" /></div>
		    <div class="bg"><img class="bg_img" src="${ctx }/static/img/bg6.jpg" /></div>
		    <div class="bg"><img class="bg_img" src="${ctx }/static/img/bg7.jpg" /></div>
	    </div>
	</div>
</body>
<script>
	layui.use(['carousel', 'element'], function() {
		var carousel = layui.carousel,element = layui.element;
		//建造实例
		carousel.render({
			elem: '#carousel',
			width: '100%' , //设置容器宽度
			full: true ,
			autoplay: true , //自动切换
            interval: 6000 ,
			indicator:'inside',
			arrow: 'hover', //始终显示箭头
			anim: 'fade' //切换动画方式
		});
	});
</script>
<script type="text/javascript">
	/*背景图片（1920*1080）随着浏览器伸缩调整代码*/
	window.onresize = function() {　　
		resize();
	};

	window.οnlοad = function() {　　
		resize();
	};

	function resize() {　 //获取浏览器的宽高，包括适配ie浏览器 
		var winWidth = (window.innerWidth) ? window.innerWidth : 
		((document.body) && (document.body.clientWidth)) ? document.body.clientWidth : 0;　　
		var winHeight = (window.innerHeight) ? window.innerHeight : 
		((document.body) && (document.body.clientHeight)) ? document.body.clientHeight :0;　　　　　　
		var bg_rate = parseFloat(1920 / 1080); //图片的宽高比 
		　　
		var br_rate = parseFloat(winWidth / winHeight); //浏览器的宽高比 
		　　 //根据宽高比的比较，固定图片的宽高来实现图片铺满屏幕而不变形，多余的部分隐藏 
		　　
		if(br_rate < bg_rate) {
			$(".bg_img").css({top:"0px",height:winHeight+"px",width:""})　　
		} else {
			$(".bg_img").css({top:"-71.5px",height:"",width:winWidth+"px"})
		}
	}
	
	/*遮盖层相关代码*/
	$(function(){
		$("#search").focus(function(){
			$(".cover").fadeIn("slow");
		})
		$(".cover").click(function(){
			$(".cover").fadeOut("slow");
		})
	});

	var type = "1";//默认精准查询
	
	$(function(){
		$("#tab_jz").click(function(){
			type = "1";
			$("#tab_jz").removeClass("tab_unselected");
			$("#tab_jz").addClass("tab_selected");
			$("#tab_zn").removeClass("tab_selected");
			$("#tab_zn").addClass("tab_unselected");
		});
		$("#tab_zn").click(function(){
			type = "2";
			$("#tab_zn").removeClass("tab_unselected");
			$("#tab_zn").addClass("tab_selected");
			$("#tab_jz").removeClass("tab_selected");
			$("#tab_jz").addClass("tab_unselected");
		});
	})
	
	$('#search').bind('input propertychange', function() {
		$(".search_suggest").fadeIn("slow");
	});
	
	$(document).keyup(function(event){
		if(event.keyCode == 13){
		  	goToSearchResult();
		}
	});

	$("#search_btn").click(function(){
		goToSearchResult();
	});
	
	$(document).ready(function(){
		$(":checkbox").labelauty();

		$("#search").bigAutocomplete({
	        url : '${ctx}/officeInfo/queryList',
	        callback:function(data){
	        	var indexs = [];
	            $("input:checkbox[name='checkbox']:checked").each(function(i){
	            	indexs.push($(this).val());
	          	});
	            window.location.href='${ctx}/officeInfo/goToSearchResult?searchName='+encodeURI(data.title)+'&type='+type+'&dataType='+encodeURI(indexs);
	        }
	    });
	});

	$("#all").click(function(){ 
		if($(this).attr("checked")){
			$(this).removeAttr("checked");
			$("input[name='checkbox']").prop("checked",false);
		}else {
			$(this).attr("checked",true);
			$("input[name='checkbox']").prop("checked",true); 
		}
	});

	function goToSearchResult(){
		var searchName = $("#search").val();
        //if (searchName == ""||searchName.replace(/(^\s*)|(\s*$)/g, "")=="") {
        //    window.location.reload();
        //}else{
        	var indexs = [];
            /* $("input:checkbox[name='checkbox']:checked").each(function(i){
            	indexs.push($(this).val());
          	}); */
          	indexs.push("y9_fawen");
          	indexs.push("y9_shouwen");
          	indexs.push("y9_chufawen");
          	indexs.push("y9_neibuqianbao");
        	window.location.href='${ctx}/officeInfo/goToSearchResult?searchName='+encodeURI(searchName)+'&type='+type+'&dataType='+encodeURI(indexs);
		//}
  	}
</script>
</html>