package net.risesoft.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface KeywordFrequencyService {
	
	void save(String keyword);
	
	Map<String, Object> searchFrequency(String keyword,String sort,String sortOrder,Integer isOpen, Integer page, Integer rows) throws IOException;

	void delFrequency(String[] ids);
	
	void disabledFrequency(String[] ids);
	
	/**
	 * 导出
	 * @return
	 * @throws IOException 
	 */
	void exportFrequency(HttpServletResponse response, HttpServletRequest request) throws IOException;
}
