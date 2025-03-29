package net.risesoft.controller.rest;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.*;

import net.risesoft.example.Message;
import net.risesoft.service.KeywordFrequencyService;
import net.risesoft.y9.json.Y9JacksonUtil;
import net.risesoft.y9.util.Y9Util;

@RestController
@RequestMapping("/rest/backstage")
@CrossOrigin
public class RestBackStageController {
	
	@Resource(name = "keywordFrequencyService")
	private KeywordFrequencyService keywordFrequencyService;

	/**
	 * 词频检索
	 * @param keyword  检索词
	 * @param isOpen  0公开 1非公开 不传 全部
	 * @param page
	 * @param limit
	 * @return
	 */
	@GetMapping(value = "/searchFrequency")
	public void searchFrequency(
			String keyword,Integer isOpen,String sort,String sortOrder,
			Integer page, Integer limit,HttpServletResponse response) 
	{
		Message message = new Message();
		message.setCode(Message.STATUS_FAIL);
		message.setMsg("检索失败");
		try {
			Map<String, Object> map = keywordFrequencyService.searchFrequency(keyword, sort, sortOrder, isOpen, page, limit);
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
	 * 词频删除
	 * ids
	 * @return
	 */
	@PostMapping(value = "/delFrequency")
	public void delFrequency(String ids,HttpServletResponse response) {
		Message message = new Message();
		message.setCode(Message.STATUS_FAIL);
		message.setMsg("删除失败");
		try {
			keywordFrequencyService.delFrequency(ids.split(","));
			message.setCode(Message.STATUS_SUCCESS);
			message.setMsg("删除成功");
		} catch (Exception e) {
			message.setCode(Message.STATUS_FAIL);
			message.setMsg("删除异常");
			e.printStackTrace();
		}
		Y9Util.renderJson(response, Y9JacksonUtil.writeValueAsString(message));
		return;
	}
	
	/**
	 * 启用/禁用切换
	 * @param ids
	 * @return
	 */
	@PostMapping(value = "/disabledFrequency")
	@ResponseBody
	public void disabledFrequency(String ids,HttpServletResponse response) {
		Message message = new Message();
		message.setCode(Message.STATUS_FAIL);
		message.setMsg("失败");
		try {
			keywordFrequencyService.disabledFrequency(ids.split(","));
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
	 * 导出
	 * @return
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping(value = "/exportFrequency")
	public void exportFrequency(HttpServletResponse response, HttpServletRequest request) throws IOException {
		keywordFrequencyService.exportFrequency(response, request);
	}
	
}
