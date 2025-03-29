package net.risesoft.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.risesoft.controller.dto.DataLogDTO;
import net.risesoft.controller.dto.SearchLog;
import net.risesoft.nosql.elastic.entity.Log.DataExtractLogInfo;
import net.risesoft.service.DataExtractLogService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.risesoft.example.DatacenterEnum;
import net.risesoft.nosql.elastic.entity.Article.ArticleInfo;
import net.risesoft.nosql.elastic.repository.DataExtractLogInfoRepository;
import net.risesoft.nosql.elastic.repository.ArticleInfoRepository;
import net.risesoft.y9.json.Y9JacksonUtil;

@Service(value = "dataExtractLogService")
@Transactional(readOnly = true)
public class DataExtractLogServiceImpl implements DataExtractLogService {

	@Autowired
	private DataExtractLogInfoRepository dataExtractLogInfoRepository;
	
	@Autowired
	private RestHighLevelClient elasticsearchClient;

	@Autowired
    private ArticleInfoRepository articleInfoRepository;
	
	@Override
	public Map<String, Object> searchDataExtractLogInfo(SearchLog searchLog, Integer page, Integer rows) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("success", true);
		try {
			if (page < 1) {
				page = 1;
			}
			// 创建查询索引
			SearchRequest searchRequest = new SearchRequest(DatacenterEnum.DATAEXTRACTINGLOG.getValue()).searchType(SearchType.DFS_QUERY_THEN_FETCH);
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			// 设置查询关键词
			BoolQueryBuilder query = new BoolQueryBuilder();
			if(StringUtils.isNotBlank(searchLog.getId())) {
				query.must(QueryBuilders.matchQuery("id", searchLog.getId()));
			}
			if(StringUtils.isNotBlank(searchLog.getTitle())) {
				query.must(QueryBuilders.matchPhrasePrefixQuery("title", searchLog.getTitle()));
			}
			if(StringUtils.isNotBlank(searchLog.getIsOpen())) {
				query.must(QueryBuilders.termsQuery("isOpen", searchLog.getIsOpen().equals("open")?true:false ));
			}
			if(StringUtils.isNotBlank(searchLog.getDataName())) {
				String[] dataNames = searchLog.getDataName().split(",");
				BoolQueryBuilder dnqb = QueryBuilders.boolQuery();
				for(String name : dataNames) {
					dnqb.should(QueryBuilders.termQuery("dataType", name));
				}
				query.must(dnqb);
			}

			searchSourceBuilder.query(query).trackTotalHits(true);
			//时间倒序
			searchSourceBuilder.sort("leadInTime.keyword", SortOrder.DESC);
			// 设置一个可选的超时，控制允许搜索的时间. 
			searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
			// 分页
			searchSourceBuilder.from((page - 1) * rows);
			searchSourceBuilder.size(rows);
			// 加入请求体
			searchRequest.source(searchSourceBuilder);
			// 发送请求
			SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
			//获取查询总数量
			SearchRequest request = new SearchRequest(DatacenterEnum.DATAEXTRACTINGLOG.getValue()).source(new SearchSourceBuilder().query(query).trackTotalHits(true));
			long count = elasticsearchClient.search(request, RequestOptions.DEFAULT).getHits().getTotalHits().value;
			// 获取搜索的文档结果
			SearchHits searchHits = response.getHits();
			SearchHit[] hits = searchHits.getHits();

			List<DataLogDTO> logDTOS = new ArrayList<>();
			for (int i = 0; i < hits.length; i++) {
				SearchHit hit = hits[i];
				String json = hit.getSourceAsString();// 将文档中的每一个对象转换json串值
				DataLogDTO info = Y9JacksonUtil.readValue(json, DataLogDTO.class);
				logDTOS.add(info);
			}
			long countAll = elasticsearchClient.search(new SearchRequest(DatacenterEnum.DATAEXTRACTINGLOG.getValue()).source(new SearchSourceBuilder().trackTotalHits(true)), RequestOptions.DEFAULT).getHits().getTotalHits().value;
			dataMap.put("countAll", countAll);
			dataMap.put("pages", (count + rows - 1) / rows);
			dataMap.put("total", count);
			dataMap.put("dataList", logDTOS);
		} catch (Exception e) {
			dataMap.put("success", false);
			e.printStackTrace();
		}
		return dataMap;
	}

	@Override
	public void publicOrNot(String id) {
		DataExtractLogInfo dataExtractLogInfo = dataExtractLogInfoRepository.findById(id).orElse(null);
		ArticleInfo articleInfo = articleInfoRepository.findById(id).orElse(null);
		if( articleInfo != null && dataExtractLogInfo != null) {
			articleInfo.setIsOpen(!articleInfo.getIsOpen());
			articleInfoRepository.save(articleInfo);
			dataExtractLogInfo.setIsOpen(!dataExtractLogInfo.getIsOpen());
			dataExtractLogInfoRepository.save(dataExtractLogInfo);
		}
	}

}
