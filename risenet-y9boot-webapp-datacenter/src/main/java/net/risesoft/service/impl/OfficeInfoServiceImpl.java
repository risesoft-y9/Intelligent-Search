package net.risesoft.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.risesoft.controller.dto.ArticleDTO;
import net.risesoft.controller.dto.SearchOfficeDTO;
import net.risesoft.service.KeywordFrequencyService;
import net.risesoft.service.OfficeInfoService;
import net.risesoft.util.CopyUtil;
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

import lombok.extern.slf4j.Slf4j;
import net.risesoft.example.DatacenterEnum;
import net.risesoft.nosql.elastic.entity.DataTypeInfo;
import net.risesoft.nosql.elastic.entity.Article.ArticleInfo;
import net.risesoft.nosql.elastic.repository.ArticleInfoRepository;
import net.risesoft.service.DataTypeInfoService;
import net.risesoft.util.CalendarUtil;
import net.risesoft.y9.json.Y9JacksonUtil;

@Service(value = "officeInfoService")
@Transactional(readOnly = true)
@Slf4j
public class OfficeInfoServiceImpl implements OfficeInfoService {

	@Autowired
	private RestHighLevelClient elasticsearchClient;

    
    @Autowired
    private ArticleInfoRepository articleInfoRepository;

    @Autowired
	private DataTypeInfoService dataTypeInfoService;

    @Autowired
	private KeywordFrequencyService keywordFrequencyService;
    
    private static final String DATE_TIME_PATTERN = "\\d{4}-\\d{2}";

	private BoolQueryBuilder queryBuilder(SearchOfficeDTO searchOfficeDTO) {
  		BoolQueryBuilder query = new BoolQueryBuilder();
  		if(StringUtils.isNotBlank(searchOfficeDTO.getSearchContent())) {
  			String[] texts = searchOfficeDTO.getSearchContent().split(" ");
  			for(String text : texts) {
  				if(StringUtils.isNotBlank(text)) {
  					BoolQueryBuilder bq = QueryBuilders.boolQuery();
					if("1".equals(searchOfficeDTO.getType())) {//精准查询
						bq.should(QueryBuilders.matchPhrasePrefixQuery("title", text).boost(6.0f));
						bq.should(QueryBuilders.matchPhrasePrefixQuery("content", text).boost(6.0f));
						bq.should(QueryBuilders.matchPhrasePrefixQuery("allContent", text));
						query.must(bq);
					}else {//智能查询
						bq.should(QueryBuilders.multiMatchQuery(text, "title", "content","allContent").minimumShouldMatch("3<70%"));
						query.must(bq);
					}
					if(text.length() > 1) {
						keywordFrequencyService.save(text);	
					}
				}
  			}
  		}
  		
  		query.must(QueryBuilders.termsQuery("isOpen", true));
  		query.must(QueryBuilders.termsQuery("dataType.keyword", getDataTypeIdList(searchOfficeDTO.getDataType())));
  		
  		if(StringUtils.isNotBlank(searchOfficeDTO.getTimeType())) {
			Pattern pattern = Pattern.compile(DATE_TIME_PATTERN);
			String[] times = searchOfficeDTO.getTimeType().split(" - ");
			if(StringUtils.isNotBlank(times[0]) && pattern.matcher(times[0]).matches() && StringUtils.isNotBlank(times[1]) && pattern.matcher(times[1]).matches()) {
				List<String> years = CalendarUtil.getDateList(times[0], times[1]);
				query.must(QueryBuilders.termsQuery("dataTime", years));
			}
		}
  		
  		return query;
  	}
	
	public String[] getDataTypeIdList(String dataType){
		List<String> dataTypeList = new ArrayList<String>();
		String[] dataTypes = dataType.split(",");
		for(String dt : dataTypes) {
			DataTypeInfo dtInfo = dataTypeInfoService.getDataType(dt);
			if(dtInfo != null && StringUtils.isNotBlank(dtInfo.getId())) {
				dataTypeList.add(dtInfo.getId());
			}
		}
		return dataTypeList.toArray(new String[dataTypeList.size()]);
	}
	
	@Override
	public Map<String, Object> searchOfficeInfo(SearchOfficeDTO searchOfficeDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("success", true);
		try {
			if (searchOfficeDTO.getPage() < 1) searchOfficeDTO.setPage(1);
			List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
			// 创建查询索引
			SearchRequest searchRequest = new SearchRequest(DatacenterEnum.ARTICLE.getValue()).searchType(SearchType.DFS_QUERY_THEN_FETCH);
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			// 设置查询关键词
			BoolQueryBuilder query = this.queryBuilder(searchOfficeDTO);

			//排序
			if(DatacenterEnum.TIMEASC.getValue().equals(searchOfficeDTO.getSortStr())) {
				searchSourceBuilder.sort("dataTime", SortOrder.ASC);
			}else if(DatacenterEnum.TIMEDESC.getValue().equals(searchOfficeDTO.getSortStr())) {
				searchSourceBuilder.sort("dataTime", SortOrder.DESC);
			}else if(DatacenterEnum.CLICKNUM.getValue().equals(searchOfficeDTO.getSortStr())) {
				searchSourceBuilder.sort("clickNum", SortOrder.DESC);
			}else if(StringUtils.isBlank(searchOfficeDTO.getSearchContent())){
				searchSourceBuilder.sort("dataTime", SortOrder.DESC);
			}

			searchSourceBuilder.query(query).trackTotalHits(true);
			// 设置一个可选的超时，控制允许搜索的时间. 
			searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
			// 分页
			searchSourceBuilder.from((searchOfficeDTO.getPage() - 1) * searchOfficeDTO.getRows());
			searchSourceBuilder.size(searchOfficeDTO.getRows());
			// 加入请求体
			searchRequest.source(searchSourceBuilder);
			// 发送请求
			SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
			// 获取查询总数量
			SearchRequest request = new SearchRequest(DatacenterEnum.ARTICLE.getValue()).source(new SearchSourceBuilder().query(query).trackTotalHits(true));
			long count = elasticsearchClient.search(request, RequestOptions.DEFAULT).getHits().getTotalHits().value;
			// 获取搜索的文档结果
			SearchHits searchHits = response.getHits();
			SearchHit[] hits = searchHits.getHits();
			for (int i = 0; i < hits.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				SearchHit hit = hits[i];
				String json = hit.getSourceAsString();// 将文档中的每一个对象转换json串值
				ArticleInfo info = Y9JacksonUtil.readValue(json, ArticleInfo.class);
				map.put("id", info.getId());
				map.put("dataType", info.getDataType());
				map.put("title", info.getTitle());
				map.put("content", info.getContent());
				map.put("dataTime", info.getDataTime());
				map.put("clickNum", info.getClickNum() == null ? "0" : info.getClickNum().toString());
				listMap.add(map);
			}
			long countAll = elasticsearchClient.search(new SearchRequest(DatacenterEnum.ARTICLE.getValue()).source(new SearchSourceBuilder().trackTotalHits(true)), RequestOptions.DEFAULT).getHits().getTotalHits().value;
			dataMap.put("countAll", countAll);
			dataMap.put("pages", (count + searchOfficeDTO.getRows() - 1) / searchOfficeDTO.getRows());
			dataMap.put("total", count);
			dataMap.put("dataList", listMap);
		} catch (Exception e) {
			dataMap.put("success", false);
			e.printStackTrace();
		}
		return dataMap;
	}
	
	private SearchHit[] queryBuilder(BoolQueryBuilder query ,Integer page, Integer limit) throws IOException {
		SearchRequest searchRequest = new SearchRequest(DatacenterEnum.ARTICLE.getValue()).searchType(SearchType.DFS_QUERY_THEN_FETCH);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(query).trackTotalHits(true);
		searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
		if(page != null && limit != null) {
			searchSourceBuilder.from((page - 1) * limit);
			searchSourceBuilder.size(limit);	
		}
		searchRequest.source(searchSourceBuilder);
		SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		return hits;
	}
	
	@Override
	public List<String> searchTitleResult(String keyword) throws IOException {
		SearchRequest searchRequest = new SearchRequest(DatacenterEnum.ARTICLE.getValue()).searchType(SearchType.DFS_QUERY_THEN_FETCH);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder query = new BoolQueryBuilder();
		String[] texts = keyword.split(" ");
		for(String text : texts) {
			BoolQueryBuilder bq = QueryBuilders.boolQuery();
			bq.must(QueryBuilders.matchPhrasePrefixQuery("title", text));
			query.should(bq);
		}
		BoolQueryBuilder bq = QueryBuilders.boolQuery();
		bq.should(QueryBuilders.termsQuery("isOpen", true));
		query.must(bq);
		searchSourceBuilder.query(query).trackTotalHits(true);
		searchSourceBuilder.size(10);
		searchRequest.source(searchSourceBuilder);
		SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		List<String> reList = new ArrayList<String>();
		for (int i = 0; i < hits.length; i++) {
			SearchHit hit = hits[i];
			String json = hit.getSourceAsString();// 将文档中的每一个对象转换json串值
			String index = hit.getIndex();//获取当前数据的索引名称
			ArticleInfo info = Y9JacksonUtil.readValue(json, ArticleInfo.class);
			if(StringUtils.isNotBlank(info.getTitle())) reList.add(info.getTitle());
		}
		return reList;
	}
	
	
	@Override
	public Map<String, Object> searchTitleResult(String keyword, String dataType, String id) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		try {
			SearchHit[] hits = null;

			/* ===== 90%相似度匹配 开始 ===== */
			if(hits == null || hits.length == 0  || (hits.length == 1 && StringUtils.isNotBlank(id))) {
				System.out.println("=====90%相似度匹配=====   dataType: "+dataType+"   keyword: "+keyword);
				BoolQueryBuilder query = searchTitleResultQuery(dataType);
				query.must(QueryBuilders.matchQuery("title", keyword).minimumShouldMatch("90%"));
				hits = queryBuilder(query, 1, 10);
			}
			/* ===== 90%相似度匹配 结束 ===== */

			/* ===== 70%相似度匹配 开始 ===== */
			if(hits.length == 0  || (hits.length == 1 && StringUtils.isNotBlank(id))) {
				System.out.println("=====70%相似度匹配=====   dataType: "+dataType+"   keyword: "+keyword);
				BoolQueryBuilder query = searchTitleResultQuery(dataType);
				query.must(QueryBuilders.matchQuery("title", keyword).minimumShouldMatch("70%"));
				hits = queryBuilder(query, 1, 10);
			}
			/* ===== 70%相似度匹配 结束 ===== */
			
			for (int i = 0; i < hits.length; i++) {
				SearchHit hit = hits[i];
				String json = hit.getSourceAsString();// 将文档中的每一个对象转换json串值

				ArticleInfo info = Y9JacksonUtil.readValue(json, ArticleInfo.class);
				if(StringUtils.isNotBlank(id) && id.equals(info.getId())) continue;
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", info.getId());
				map.put("title", info.getTitle());
				map.put("dataType", info.getDataType());
				map.put("dataTime", info.getDataTime());
				listMap.add(map);
			}
			
			if(listMap.size() > 10) listMap = listMap.subList(0,10);
			dataMap.put("success", true);
			dataMap.put("rows", listMap);
		} catch (Exception e) {
			dataMap.put("success", false);
			e.printStackTrace();
		}
		return dataMap;
	}
	
	public BoolQueryBuilder searchTitleResultQuery(String dataType){
		BoolQueryBuilder query = new BoolQueryBuilder();
		query.must(QueryBuilders.termsQuery("isOpen", true));
		if(StringUtils.isNotBlank(dataType)) {
			query.must(QueryBuilders.termsQuery("dataType", dataType));
		}
		return query;
	}

	@Override
	public ArticleDTO textdisplay(String id, String dataType, String keyword){
		if (StringUtils.isBlank(id)) return null;
		ArticleDTO articleDTO = new ArticleDTO();
		ArticleInfo article = articleInfoRepository.findById(id).orElse(null);
		if(article != null) {
			article.setClickNum(article.getClickNum() == null ? 1 : article.getClickNum() + 1);
			articleInfoRepository.save(article);
			CopyUtil.articleInfoToArticleDTO(article,articleDTO);
		}
		return articleDTO;
	}
	
	@Override
	public void download(String url, String fileName, HttpServletResponse response, HttpServletRequest request) {
		OutputStream out = null;
		try {
			URL fileUrl = new URL(url);
			URLConnection connection = fileUrl.openConnection();
			InputStream input = connection.getInputStream();
			byte[] buffer = new byte[1024];  
	        int len = 0;
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
	        while((len = input.read(buffer)) != -1) {  
	            bos.write(buffer, 0, len);  
	        }
	        bos.close();
	        byte[] getData = bos.toByteArray();
			if(request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0){
				fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");//火狐浏览器
		    }else if(request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0){
		    	fileName = URLEncoder.encode(fileName, "UTF-8");//IE浏览器
		    }else{
		    	fileName = URLEncoder.encode(fileName, "UTF-8");//IE浏览器
		    }
			out = response.getOutputStream();
		    response.reset();  
		    response.setHeader("Content-disposition", "attachment; filename=\""+fileName+"\"" );
		    response.setHeader("Content-type", "text/html;charset=UTF-8");
		    response.setContentType("application/octet-stream");  
		    // 允许跨域访问的域名：若有端口需写全（协议+域名+端口），若没有端口末尾不用加'/'
		    response.setHeader("Access-Control-Allow-Origin", "*");
		    out.write(getData);
		} catch (Exception e) {
			log.error("Broken pipe 错误 /r/n" + "文件名: " + fileName);
			//e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
