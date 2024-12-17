package net.risesoft.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.risesoft.controller.dto.UserUsageLogDTO;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.risesoft.example.DatacenterEnum;
import net.risesoft.nosql.elastic.entity.Log.UserUsageLog;
import net.risesoft.nosql.elastic.repository.UserUsageLogRepository;
import net.risesoft.service.UserUsageLogService;
import net.risesoft.sso.filter.LoginUserHolder;
import net.risesoft.sso.model.oauth2.UserInfo;
import net.risesoft.y9.json.Y9JacksonUtil;

@Service(value = "userUsageLogService")
public class UserUsageLogServiceImpl implements UserUsageLogService{
	
	@Autowired
    private UserUsageLogRepository userUsageLogRepository;
	
	@Autowired
	private RestHighLevelClient elasticsearchClient;

	@Override
	public void save(UserUsageLog uul) {
		uul.setId(UUID.randomUUID().toString());
		UserInfo userinfo = LoginUserHolder.getUserInfo();
		if(userinfo != null && StringUtils.isBlank(uul.getUserId())) {
			uul.setUserId(userinfo.getPersonID());
			uul.setUserName(userinfo.getName());
			uul.setTenantId(userinfo.getTenantID());
		}
		uul.setModularName("全文检索");
		uul.setOperateType("查看");
		uul.setLogLevel("RSLOG");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		//uul.setLogTime(sdf.format(new Date()));
		Calendar calendar = Calendar.getInstance();
        // 加8小时
        calendar.add(Calendar.HOUR_OF_DAY, 8);
		uul.setLogTime(calendar.getTime());
		userUsageLogRepository.save(uul);
	}
	
	@Override
	public Map<String, Object> searchUserUsageLog(UserUsageLogDTO uulDTO, Integer page, Integer rows) throws Exception {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		if (page < 1) {
			page = 1;
		}
		// 创建查询索引
		SearchRequest searchRequest = new SearchRequest(DatacenterEnum.USERUSAGELOG.getValue()).searchType(SearchType.DFS_QUERY_THEN_FETCH);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 设置查询关键词
		BoolQueryBuilder query = new BoolQueryBuilder();
		if(StringUtils.isNotBlank(uulDTO.getUserName())) {
			query.must(QueryBuilders.matchPhrasePrefixQuery("userName", uulDTO.getUserName() ));
		}
		if(StringUtils.isNotBlank(uulDTO.getUserHostIp())) {
			query.must(QueryBuilders.matchPhrasePrefixQuery("userHostIp", uulDTO.getUserHostIp() ));
		}
		if(StringUtils.isNotBlank(uulDTO.getModularName())) {
			query.must(QueryBuilders.matchPhrasePrefixQuery("modularName", uulDTO.getModularName() ));
		}
		if(StringUtils.isNotBlank(uulDTO.getOperateName())) {
			query.must(QueryBuilders.matchPhrasePrefixQuery("operateName", uulDTO.getOperateName() ));
		}
		if(StringUtils.isNotBlank(uulDTO.getOperateType())) {
			query.must(QueryBuilders.termsQuery("operateType", uulDTO.getOperateType() ));
		}
		if(StringUtils.isNotBlank(uulDTO.getSuccess())) {
			query.must(QueryBuilders.termsQuery("success", uulDTO.getSuccess() ));
		}
		if(StringUtils.isNotBlank(uulDTO.getLogLevel())) {
			query.must(QueryBuilders.termsQuery("logLevel", uulDTO.getLogLevel() ));
		}
		if(StringUtils.isNotBlank(uulDTO.getStartDate()) && StringUtils.isNotBlank(uulDTO.getEndDate())) {
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(f1.parse(uulDTO.getEndDate()));
	        calendar.add(Calendar.DATE, 1);
		        
		    String startDate = f2.format(f1.parse(uulDTO.getStartDate()));
		    String endDate = f2.format(calendar.getTime());
			query.must(QueryBuilders.rangeQuery("logTime").from(startDate).to(endDate));	
		}
		
		searchSourceBuilder.query(query).trackTotalHits(true);
		//时间倒序
		searchSourceBuilder.sort("logTime.keyword", SortOrder.DESC);
		// 分页
		searchSourceBuilder.from((page - 1) * rows);
		searchSourceBuilder.size(rows);
		// 加入请求体
		searchRequest.source(searchSourceBuilder);
		// 发送请求
		SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
		//获取查询总数量
		SearchRequest request = new SearchRequest(DatacenterEnum.USERUSAGELOG.getValue()).source(new SearchSourceBuilder().query(query).trackTotalHits(true));
		long count = elasticsearchClient.search(request, RequestOptions.DEFAULT).getHits().getTotalHits().value;
		// 获取搜索的文档结果
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		List<UserUsageLogDTO> list = new ArrayList<UserUsageLogDTO>();
		for (int i = 0; i < hits.length; i++) {
			SearchHit hit = hits[i];
			String json = hit.getSourceAsString();// 将文档中的每一个对象转换json串值
			UserUsageLogDTO info = Y9JacksonUtil.readValue(json, UserUsageLogDTO.class);
			list.add(info);
		}
		long countAll = elasticsearchClient.search(new SearchRequest(DatacenterEnum.USERUSAGELOG.getValue()).source(new SearchSourceBuilder().trackTotalHits(true)), RequestOptions.DEFAULT).getHits().getTotalHits().value;
		dataMap.put("countAll", countAll);
		dataMap.put("pages", (count + rows - 1) / rows);
		dataMap.put("total", count);
		dataMap.put("dataList", list);
		return dataMap;
	}

}
