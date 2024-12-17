package net.risesoft.service;

import net.risesoft.controller.dto.SearchLog;

import java.util.Map;

public interface DataExtractLogService {
	
	/**
	 * 全文检索
	 * 
	 * @return
	 */
	Map<String, Object> searchDataExtractLogInfo(SearchLog searchLog, Integer page, Integer rows);
	
	/**
	 * 是否公开切换
	 * 
	 * @return
	 */
	void publicOrNot(String id);

}
