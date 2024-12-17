<%@ include file="/static/common/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<div class="demoTable" style="padding-top: 5px;">
	<div class="layui-form-item">
 		<div class="layui-inline">
     		<label class="layui-form-label" style="width: 30px;">主题</label>
     		<div class="layui-input-inline" style="margin-top: 5px;width: 150px;">
         		<select id="topic" style="height: 30px;">
				    <option value=""></option>
				    <option value="y9_officeinfo">y9_officeinfo</option>
				    <option value="y9_historyinfo">y9_historyinfo</option>
				    <option value="y9_eforminfo">y9_eforminfo</option>
				    <option value="y9_subscriptioninfo">y9_subscriptioninfo</option>
				    <option value="y9_cmsinfo">y9_cmsinfo</option>
				</select>
     		</div>
 		</div>
 		<div class="layui-inline">
		     <label class="layui-form-label" style="width: 30px;">结果</label>
		     <div class="layui-input-inline" style="margin-top: 5px;width: 100px;">
         		<select id="message" style="height: 30px;">
			        <option value=""></option>
			        <option value="success">success</option>
			        <option value="false">false</option>
			    </select>
         	</div>
     	</div>
     	<div class="layui-inline" >
         	<div class="layui-btn-group">
             	<button class="layui-btn layui-btn-warm" data-type="reload" style="border-radius: 3px;line-height: 30px;height: 30px;">查询</button>
         	</div>
     	</div>
    </div>
</div>
<table class="layui-hide" id="test" lay-filter="demo"></table>
<script type="text/javascript">
layui.use(['element','table'], function(){
  	var element = layui.element,table = layui.table,layer = layui.layer;
  	
  	table.render({
        elem: '#test',
        url:'${ctx}/dataCenter/showAll',
        page:true,
        limit:20,
        cols: [[
        	{type: 'numbers', align: 'center', title:'序号'},
            {field:'topic', align: 'center',title: '主题',width:140,sort: true},
            {field:'context', align: 'center',title: '内容', width: 750},
            {field:'message', align: 'center',title: '结果',width: 700,sort: true},
            {field:'createDate', align: 'center',title: '创建时间',width:180,sort: true},
        ]],
        id: 'dataReload',
        height: 'full-250'
    });
  	
  	var $ = layui.$, active = {
        reload: function(){
            var topic = $('#topic').val();
            var message = $('#message').val();
          	//执行重载
           	table.reload('dataReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                	topic: topic,
                	message: message
                }
            });
        }
    };

    $('.demoTable .layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});
</script>