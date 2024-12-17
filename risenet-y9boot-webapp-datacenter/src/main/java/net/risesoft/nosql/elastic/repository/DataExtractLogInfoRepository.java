package net.risesoft.nosql.elastic.repository;

import java.util.List;

import net.risesoft.nosql.elastic.entity.Log.DataExtractLogInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface DataExtractLogInfoRepository extends ElasticsearchRepository<DataExtractLogInfo,String>{
	
	List<DataExtractLogInfo> findByLeadInTime(String leadInTime);
	
}
