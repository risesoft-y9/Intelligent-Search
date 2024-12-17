package net.risesoft.controller.rest;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.risesoft.controller.dto.UserUsageLogDTO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.risesoft.example.Message;
import net.risesoft.service.UserUsageLogService;
import net.risesoft.y9.json.Y9JacksonUtil;
import net.risesoft.y9.util.Y9Util;

@RestController
@RequestMapping("/rest/userUsageLog")
@CrossOrigin
public class RestUserUsageLogController {

	@Resource(name = "userUsageLogService")
	private UserUsageLogService userUsageLogService;
	
	/**
	 * 日志检索
	 * @param userUsageLog
	 * @param page
	 * @param limit
	 * @return
	 */
	@GetMapping(value = "/searchUserUsageLog")
	public void searchUserUsageLog(
			UserUsageLogDTO userUsageLog,
			Integer page, 
			Integer limit,
			HttpServletResponse response) {
		Message message = new Message();
		message.setCode(Message.STATUS_FAIL);
		message.setMsg("检索失败");
		try {
			Map<String, Object> map = userUsageLogService.searchUserUsageLog(userUsageLog, page, limit);
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
	
}
