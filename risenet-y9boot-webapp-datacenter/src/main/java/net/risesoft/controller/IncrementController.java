package net.risesoft.controller;

import javax.servlet.http.HttpServletResponse;

import net.risesoft.example.Message;
import net.risesoft.y9.util.Y9Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.risesoft.service.IncrementService;
import net.risesoft.y9.json.Y9JacksonUtil;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping(value = "/increment")
public class IncrementController {

    @Autowired
    private IncrementService incrementService;


	@RequestMapping(value = "/importData")
	@ResponseBody
	public void importData(MultipartFile file, HttpServletResponse response) {
		Message message = new Message();
		message.setCode(Message.STATUS_FAIL);
		message.setMsg("失败");
		try {
			incrementService.importDate(file, response);
			message.setMsg("成功");
			message.setCode(Message.STATUS_SUCCESS);
		} catch (Exception e) {
			message.setMsg("异常");
			e.printStackTrace();
		}
		Y9Util.renderJson(response, Y9JacksonUtil.writeValueAsString(message));
	}

}