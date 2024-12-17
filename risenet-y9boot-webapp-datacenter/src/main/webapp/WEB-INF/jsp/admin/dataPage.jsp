<%@ include file="/static/common/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<div class="demoTable2" style="padding-top: 5px;">
    <div class="layui-form-item">
	     <div class="layui-inline">
	         <label class="layui-form-label" style="width: 50px;">搜索词</label>
	         <div class="layui-input-block" style="width: 600px;margin-left: 80px;margin-top: 6px;">
	             <input type="text" class="layui-input" id="searchName" style="height: 30px;">
	         </div>
	     </div>
	     <div class="layui-inline">
	         <div class="layui-btn-group">
	             <button class="layui-btn layui-btn-warm" data-type="reload" style="border-radius: 3px;line-height: 30px;height: 30px;">查询</button>
	         </div>
	     </div>
    </div>
</div>
<table class="layui-hide" id="test2" lay-filter="user"></table>
<script type="text/javascript">
	layui.use('table', function(){
	  	var table = layui.table,form = layui.form,layer = layui.layer;
	  
	  	//方法级渲染
	  	table.render({
	  		elem: '#test2',
            url:'${ctx}/dataCenter/showAll2',
            page:true,
            limit:20,
            cols: [[
            	{type: 'numbers', align: 'center', title:'序号'},
            	{field:'appCNName', align: 'center',title: '应用', width: 120},
                {field:'title', align: 'center',title: '标题',event: 'open',width:550},
                {field:'zy', align: 'center',title: '正文/流程ID',width:900},
                {field:'startTime', align: 'center',title: '开始时间',width:120},
                {title:'操作',align: 'center',toolbar: '#barDemo2',width:80}
            ]],
            id: 'dataReload2',
            height: 'full-250'
	 	});
	  	
	  	var $ = layui.$, active = {
            reload: function(){
                var searchName = $('#searchName').val();
              	//执行重载
               	table.reload('dataReload2', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    },
                    where: {
                    	searchName: searchName
                    }
                });
            }
        };
			
		$('.demoTable2 .layui-btn').on('click', function(){
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });
			
		//监听工具条
        table.on('tool(user)', function(obj){
             var data = obj.data;
             if(obj.event === 'open'&&data.dataType=='banjian'){
             	var index = layer.open({
       		      type: 2,
       		      title: "信息列表",
       		      shadeClose: true, //点击遮罩关闭层
       		      closeBtn: false,
       		      area: ['70%', '80%'],
       		      content: '${ctx}/dataCenter/listPage2?id='+data.zy,
       		    });
             }
             if(obj.event === 'delete'){
             	layer.confirm('确定要删除该信息吗？', {
     			    btn: ['确定','取消'],  //按钮
     			    closeBtn: false,
     			    shadeClose: true, //点击遮罩关闭层
     			}, function(){
            	  	$.ajax({
	      				type: 'POST',
	      				dataType: 'json',
	      				data: {
	      					id: data.id,
	      					type: data.dataType
	      				},
	      				url: '${ctx}/dataCenter/deleteInfo',
	      				success: function(res) {
	      					if (res.success) {
	      						//执行重载
	      	                   	table.reload('dataReload2', {
	      	                        page: {
	      	                            curr: 1 //重新从第 1 页开始
	      	                        }
	      	                    });
	      	                   	layer.closeAll();
	      					}else{
	      						alert(res.msg);
	      					}
	      				}
	      			});
     			}, function(){
     				layer.closeAll();
     			});
             }
         });
	});

</script>
<script type="text/html" id="barDemo2">
  	<a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="delete">删除</a>
</script>