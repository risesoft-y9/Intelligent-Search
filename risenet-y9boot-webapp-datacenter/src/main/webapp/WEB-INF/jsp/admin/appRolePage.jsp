<%@ include file="/static/common/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<table class="layui-hide" id="table_user" lay-filter="user"></table>	
<script type="text/javascript">
	layui.use('table', function(){
	  	var table = layui.table,form = layui.form,layer = layui.layer;
	  
	  	//方法级渲染
	  	table.render({
		    elem: '#table_user',
		    url: '${ctx}/dataCenter/appList',
		    cols: [[
		    	{type: 'numbers', align: 'center', title:'序号'},
	          	{field:'name', align: 'center',title: '应用'},
	          	{title:'操作',align: 'center',toolbar: '#barDemo'},
	          	{field:'state', align: 'center',title: '是否公开',templet: '#switchTpl', unresize: true}
		    ]],
		    id: 'dataReload',
		    limit:20,
		    height: 'full-200',
		    page: false
	 	});
	  	
	  	//监听工具条
        table.on('tool(user)', function(obj){
            var data = obj.data;
            if(obj.event === 'info'&&data.name!='部门号'&&data.name!='新闻'){
            	var index = layer.open({
          	      	type: 2,
          	      	title: "信息列表",
          	      	shadeClose: true, //点击遮罩关闭层
          	      	closeBtn: false,
          	      	area: ['75%', '80%'],
          	      	content: '${ctx}/dataCenter/listPage?appName='+data.name,
          	    });
            }else{
            	alert("该应用暂不支持！");
            }
        });
	  	
	  	//监听操作
        form.on('switch(state)', function(obj){
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
					id: id
				},
				url: '${ctx}/dataCenter/saveState',
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
<script type="text/html" id="barDemo">
  	<a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="info">详情</a>
</script>
<script type="text/html" id="switchTpl">
  	<input type="checkbox" name="state" value="{{d.id}}" lay-skin="switch" lay-text="是|否" lay-filter="state" {{ d.state == 1 ? 'checked' : '' }}>
</script>