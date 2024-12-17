package net.risesoft.nosql.elastic.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import net.risesoft.nosql.elastic.entity.Log.KeywordFrequency;

public interface KeywordFrequencyRepository extends ElasticsearchRepository<KeywordFrequency,String>{

}
