package net.risesoft.controller.rest;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.risesoft.controller.dto.SearchLog;
import org.springframework.web.bind.annotation.*;

import net.risesoft.example.Message;
import net.risesoft.nosql.elastic.entity.DataTypeInfo;
import net.risesoft.service.DataExtractLogService;
import net.risesoft.service.DataTypeInfoService;
import net.risesoft.y9.json.Y9JacksonUtil;
import net.risesoft.y9.util.Y9Util;

@RestController
@RequestMapping("/rest/dataExtractLog")
@CrossOrigin
public class RestDataExtractLogController {
	
	@Resource(name = "dataExtractLogService")
	private DataExtractLogService dataExtractLogService;
	
	@Resource(name = "dataTypeInfoService")
	private DataTypeInfoService dataTypeInfoService;
	
	/**
	 * 日志检索
	 * @param searchLog
	 * @param page
	 * @param limit
	 * @return
	 */
	@GetMapping(value = "/searchDataExtractLogInfo")
	public void searchDataExtractingLogInfo(
			SearchLog searchLog,
			Integer page, 
			Integer limit,
			HttpServletResponse response) 
	{
		Message message = new Message();
		message.setCode(Message.STATUS_FAIL);
		message.setMsg("检索失败");
		try {
			Map<String, Object> map = dataExtractLogService.searchDataExtractLogInfo(searchLog, page, limit);
			message.setData(map);
			message.setCode(Message.STATUS_SUCCESS);
			message.setMsg("检索成功");
		} catch (Exception e) {
			message.setCode(Message.STATUS_FAIL);
			message.setMsg("检索异常");
			e.printStackTrace();
		}
		Y9Util.renderJson(response, Y9JacksonUtil.writeValueAsString(message));
		return;
	}
	
	/**
	 * 是否公开切换
	 * @param id
	 * @return
	 */
	@PostMapping(value = "/publicOrNot")
	@ResponseBody
	public void publicOrNot(String id,HttpServletResponse response) {
		Message message = new Message();
		message.setCode(Message.STATUS_FAIL);
		message.setMsg("失败");
		try {
			dataExtractLogService.publicOrNot(id);
			message.setMsg("成功");
			message.setCode(Message.STATUS_SUCCESS);
		} catch (Exception e) {
			message.setCode(Message.STATUS_FAIL);
			message.setMsg("异常");
			e.printStackTrace();
		}
		Y9Util.renderJson(response, Y9JacksonUtil.writeValueAsString(message));
		return;
	}

	/**
	 * 获取数据类型
	 * @return
	 */
	@GetMapping(value = "/getDataType")
    @ResponseBody
    public void getDataType(HttpServletResponse response) {
		Message message = new Message();
		try {
			List<DataTypeInfo> list = dataTypeInfoService.getDataTypeAll();
			message.setData(list);
			message.setCode(Message.STATUS_SUCCESS);
			message.setMsg("获取成功");
		} catch (Exception e) {
			message.setCode(Message.STATUS_FAIL);
			message.setMsg("获取异常");
			e.printStackTrace();
		}
		Y9Util.renderJson(response, Y9JacksonUtil.writeValueAsString(message));
		return;
	}
	
	/**
	 * 类型屏蔽,屏蔽-显示
	 * @return
	 */
	@PostMapping(value = "/typeSwitch")
    @ResponseBody
    public void typeSwitch(String id ,HttpServletResponse response) {
		Message message = new Message();
		try {
			Boolean boo = dataTypeInfoService.typeSwitch(id);
			if(boo) {
				message.setCode(Message.STATUS_SUCCESS);
				message.setMsg("切换成功");
			}else {
				message.setCode(Message.STATUS_FAIL);
				message.setMsg("切换失败");
			}
		} catch (Exception e) {
			message.setCode(Message.STATUS_FAIL);
			message.setMsg("切换异常");
			e.printStackTrace();
		}
		Y9Util.renderJson(response, Y9JacksonUtil.writeValueAsString(message));
		return;
	}

}
