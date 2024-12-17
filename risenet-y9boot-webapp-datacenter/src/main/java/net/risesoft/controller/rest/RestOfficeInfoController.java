package net.risesoft.controller.rest;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.risesoft.controller.dto.SearchOfficeDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;

import net.risesoft.example.Message;
import net.risesoft.nosql.elastic.entity.Log.UserUsageLog;
import net.risesoft.service.DataTypeInfoService;
import net.risesoft.service.OfficeInfoService;
import net.risesoft.service.UserUsageLogService;
import net.risesoft.y9.json.Y9JacksonUtil;
import net.risesoft.y9.util.Y9Util;

@RestController
@RequestMapping("/rest/officeInfo")
@CrossOrigin
public class RestOfficeInfoController {
	
	@Resource(name = "officeInfoService")
	private OfficeInfoService officeInfoService;
	
	@Resource(name = "dataTypeInfoService")
	private DataTypeInfoService dataTypeInfoService;
	
	@Resource(name = "userUsageLogService")
	private UserUsageLogService userUsageLogService;
	
	/**
	 * 获取可查的文章类型
	 */
	@GetMapping("/getDataType")
	public void getDataType(HttpServletResponse response) {	
		Message message = new Message();
		message.setCode(Message.STATUS_FAIL);
		message.setMsg("获取失败");
		try {
			message.setData(dataTypeInfoService.getDataTypeByIsShow(true));
			message.setMsg("获取成功");
			message.setCode(Message.STATUS_SUCCESS);
		} catch (Exception e) {
			message.setMsg("获取异常");
			e.printStackTrace();
		}
		Y9Util.renderJson(response, Y9JacksonUtil.writeValueAsString(message));
		return;
	}
	
	/**
	 * 全文检索
	 * @param searchOfficeDTO
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping(value = "/searchOfficeInfo")
	public void searchOfficeInfo(SearchOfficeDTO searchOfficeDTO, HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String success = "成功";
		
		Message message = new Message();
		message.setCode(Message.STATUS_FAIL);
		message.setMsg("检索失败");
		try {
			Map<String, Object> map = officeInfoService.searchOfficeInfo(searchOfficeDTO);
			map.put("searchName", searchOfficeDTO.getSearchContent());
			message.setData(map);
			message.setCode(Message.STATUS_SUCCESS);
			message.setMsg("检索成功");
		} catch (Exception e) {
			message.setMsg("检索异常");
			success = "失败";
			e.printStackTrace();
		}
		
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		saveLog("searchOfficeInfo","检索文章列表",success,duration,JSONObject.toJSONString(searchOfficeDTO), request);
		
		Y9Util.renderJson(response, Y9JacksonUtil.writeValueAsString(message));
		return;
	}
	
	/**
	 * 打开搜索文档
	 * @param id
	 * @param dataType
	 * @param keyword
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping(value = "/textdisplay")
	public void textdisplay(
			String id, 
			String dataType, 
			String keyword, 
			HttpServletRequest request,
			HttpServletResponse response) 
	{
		long startTime = System.currentTimeMillis();
		String success = "成功";
		
		Message message = new Message();
		message.setCode(Message.STATUS_FAIL);
		message.setMsg("打开搜索文档失败");
		try {
			id=URLDecoder.decode(id, "UTF-8");
			dataType=URLDecoder.decode(dataType, "UTF-8");
			message.setData(officeInfoService.textdisplay(id, dataType, keyword));
			message.setMsg("打开搜索文档成功");
			message.setCode(Message.STATUS_SUCCESS);
		} catch (Exception e) {
			message.setMsg("打开搜索文档异常");
			success = "失败";
			e.printStackTrace();
		}
		
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		String json = "{\"id\":"+id+",\"dataType\":"+dataType+",\"keyword\":"+keyword+"}";
		saveLog("textdisplay","打开搜索文档",success,duration,json, request);
		
		Y9Util.renderJson(response, Y9JacksonUtil.writeValueAsString(message));
		return;
	}
	
	/**
	 * 搜索标题
	 * @param keyword
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping(value = "/searchTitleResult")
	public void searchTitleResult(
			String keyword,
			HttpServletRequest request,
			HttpServletResponse response) 
	{
		long startTime = System.currentTimeMillis();
		String success = "成功";
		
		Message message = new Message();
		message.setCode(Message.STATUS_FAIL);
		message.setMsg("搜索标题失败");
		try {
			List<String> list = officeInfoService.searchTitleResult(keyword);
			message.setData(list);
			message.setMsg("搜索标题成功");
			message.setCode(Message.STATUS_SUCCESS);
		} catch (Exception e) {
			message.setMsg("搜索标题异常");
			success = "失败";
			e.printStackTrace();
		}
		
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		String json = "{\"keyword\":"+keyword+"}";
		saveLog("searchTitleResult","搜索标题",success,duration,json, request);
		
		Y9Util.renderJson(response, Y9JacksonUtil.writeValueAsString(message));
		return;
	}
	
	/**
	 * 根据输入词获取相关推荐列表
	 * @param keyword
	 * @return
	 */
	@GetMapping(value = "/queryList")
	public void queryList(
			@RequestParam(required = false) String id,
			String keyword, 
			String dataType,
			HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String success = "成功";
		
		Message message = new Message();
		message.setCode(Message.STATUS_FAIL);
		message.setMsg("获取相关推荐失败");
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			Map<String, Object> map = null;
			map = officeInfoService.searchTitleResult(keyword, dataType, id);
			data.put("data", map.get("rows"));
			message.setData(data);
			message.setMsg("获取相关推荐成功");
			message.setCode(Message.STATUS_SUCCESS);
		} catch (Exception e) {
			message.setMsg("获取相关推荐异常");
			success = "失败";
			e.printStackTrace();
		}
		
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		String json = "{\"id\":"+id+",\"keyword\":"+keyword+",\"dataType\":"+dataType+"}";
		saveLog("queryList2","获取相关推荐文档",success,duration,json, request);
		
		Y9Util.renderJson(response, Y9JacksonUtil.writeValueAsString(message));
		return;
	}
	
	/**
	 * 文件下载
	 * @param url
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/download")
	public void download(@RequestParam String url, String name, HttpServletResponse response, HttpServletRequest request) throws Exception {		
		officeInfoService.download(url, name, response, request);
	}
	
	/**
	 * 文件下载记录
	 * @param url
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/downloadLog")
	public void downloadLog(@RequestParam String id,@RequestParam String url, String fileName, HttpServletResponse response, HttpServletRequest request) throws Exception {
		String json = "{\"id\":"+id+",\"url\":"+URLEncoder.encode(url, "UTF-8")+",\"fileName\":"+fileName+"}";
		saveLog("download","文件下载","",-1,json, request);
	}
	
	private void saveLog(String methodName, String OperateName, String success, long elapsedTime, String parameter, HttpServletRequest request) {
		String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        UserUsageLog uul = new UserUsageLog();
		uul.setUserHostIp(remoteAddr);
		uul.setMethodName(methodName);
		uul.setOperateName(OperateName);
		uul.setSuccess(success);
		uul.setElapsedTime(elapsedTime + "毫秒");
		uul.setParameter(parameter);
		userUsageLogService.save(uul);
	}
}
