package net.risesoft.service;

import java.util.Map;

import net.risesoft.controller.dto.UserUsageLogDTO;
import net.risesoft.nosql.elastic.entity.Log.UserUsageLog;

public interface UserUsageLogService {

	void save(UserUsageLog uul);
	
	Map<String, Object> searchUserUsageLog(UserUsageLogDTO uulDTO, Integer page, Integer rows) throws Exception;
	
}
