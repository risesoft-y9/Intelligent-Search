package net.risesoft.nosql.elastic.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import net.risesoft.nosql.elastic.entity.DataTypeInfo;

public interface DataTypeInfoRepository extends ElasticsearchRepository<DataTypeInfo,String>{
	
	List<DataTypeInfo> findByIsShowOrderByIndex(Boolean isShow);
	
	DataTypeInfo findByDataName(String dataName);
	
}
