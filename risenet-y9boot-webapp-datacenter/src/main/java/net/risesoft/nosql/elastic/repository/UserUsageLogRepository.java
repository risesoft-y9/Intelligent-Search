package net.risesoft.nosql.elastic.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import net.risesoft.nosql.elastic.entity.Log.UserUsageLog;

public interface UserUsageLogRepository  extends ElasticsearchRepository<UserUsageLog,String>{
	
	
	
}
