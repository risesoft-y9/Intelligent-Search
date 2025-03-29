<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/static/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html >
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="${ctx }/static/css/layui.css">
	<script src="${ctx}/static/js/jquery-1.10.2.min.js"></script>
	<script src="${ctx }/static/layui/layui.js" charset="utf-8"></script>
</head>
<body>
	<div class="demoTable">
  		<div class="layui-form-item" style="margin-bottom: 0px;margin-top: 15px;">
	        <div class="layui-inline">
	            <label class="layui-form-label" style="width: 30px;">标题</label>
	            <div class="layui-input-inline" style="width: 250px;margin-top: 5px;">
	                <input type="text" id="title" class="layui-input" style="height: 30px;">
	            </div>
	        </div>
	        <div class="layui-inline">
	            <label class="layui-form-label" style="width: 60px;">是否公开</label>
	            <div class="layui-input-inline" style="margin-top: 5px;;width: 60px;">
	                <select id="disabled" style="height: 30px;width: 60px;">
				        <option value=""></option>
				        <option value="1">是</option>
				        <option value="0">否</option>
				    </select>
	            </div>
	        </div>
	        <div class="layui-inline">
	            <div class="layui-btn-group">
	                <button class="layui-btn layui-btn-warm" data-type="reload" style="border-radius: 3px;line-height: 30px;height: 30px;">查询</button>
	            </div>
	        </div>
        </div>
	</div>
	<table class="layui-hide" id="table_user" lay-filter="user"></table>
	<script type="text/javascript">
		var appName = "${appName}";
		layui.use('table', function(){
		  var table = layui.table,form = layui.form;
		  
		  //方法级渲染
		  table.render({
		    elem: '#table_user',
		    url: '${ctx}/dataCenter/dataList?appName='+appName,
		    cols: [[
		    	{type: 'numbers', align: 'center', title:'序号'},
                {field:'appCNName', align: 'center',title: '应用',width: 120},
                {field:'title', align: 'center',title: '标题'},
                {field:'zy', align: 'center',title: '正文'},
                {field:'undertaker', align: 'center',title: '承办人'},
                {field:'state', align: 'center',title: '人员ID'},
                {field:'disabled', align: 'center',title: '是否公开',width: 120,templet: '#switchTpl', unresize: true}
		    ]],
		    id: 'dataReload',
		    limit:20,
		    height: 'full-100',
		    page: true
		 });
		  
		 var $ = layui.$, active = {
		    reload: function(){
		      var title = $('#title').val();
		      var disabled = $('#disabled').val();
		      //执行重载
		      table.reload('dataReload', {
		    	  page: {
                      curr: 1 //重新从第 1 页开始
                  },
		          where: {
		        	title: title,
		        	disabled:disabled
		          }
		      });
		    }
		  };
		  
		  $('.demoTable .layui-btn').on('click', function(){
	    	var type = $(this).data('type');
		    active[type] ? active[type].call(this) : '';
		  });
		  
          //监听操作
          form.on('switch(sexDemo)', function(obj){
        	  var id = this.value;
        	  var bol = obj.elem.checked;
              var state = 0;
              if(bol==true){
            	  state=1;
              }
        	  $.ajax({
    				type: 'POST',
    				dataType: 'json',
    				data: {
    					state: state,
    					id: id,
    				},
    				url: '${ctx}/dataCenter/saveState2',
    				success: function(res) {
    					if (res.success) {
    						//window.location.reload();
    					}else{
    						alert(res.msg);
    					}
    				}
    			});
          });
		});
		
	</script>
	<script type="text/html" id="switchTpl">
  		<input type="checkbox" name="disabled" value="{{d.id}}" lay-skin="switch" lay-text="是|否" lay-filter="sexDemo" {{ d.disabled == 1 ? 'checked' : '' }}>
	</script>
</body>
</html>