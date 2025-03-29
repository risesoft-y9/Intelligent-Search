package net.risesoft.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;

import net.risesoft.example.DatacenterEnum;
import net.risesoft.nosql.elastic.entity.Log.KeywordFrequency;
import net.risesoft.nosql.elastic.repository.KeywordFrequencyRepository;
import net.risesoft.service.KeywordFrequencyService;
import net.risesoft.y9.json.Y9JacksonUtil;

@Service(value = "keywordFrequencyService")
public class KeywordFrequencyServiceImpl implements KeywordFrequencyService{

	@Autowired
    private KeywordFrequencyRepository keywordFrequencyRepository;
	
	@Autowired
	private RestHighLevelClient elasticsearchClient;

	@Override
	public void save(String keyword) {
		KeywordFrequency kf = keywordFrequencyRepository.findById(keyword).orElse(null);
		if(kf == null) {
			kf = new KeywordFrequency();
			kf.setId(keyword);
			kf.setKeyword(keyword);
			kf.setFrequency(1);
			kf.setEndTime(new Date());
		}else {
			kf.setFrequency(kf.getFrequency()+1);
			kf.setEndTime(new Date());
		}
		keywordFrequencyRepository.save(kf);
	}

	@Override
	public Map<String, Object> searchFrequency(String keyword, String sort, String sortOrder, Integer isOpen, Integer page, Integer rows) throws IOException {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		if (page < 1) {
			page = 1;
		}
		// 创建查询索引
		SearchRequest searchRequest = new SearchRequest(DatacenterEnum.KEYWORKFREQUENCY.getValue()).searchType(SearchType.DFS_QUERY_THEN_FETCH);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 设置查询关键词
		BoolQueryBuilder query = new BoolQueryBuilder();
		if(StringUtils.isNotBlank(keyword)) {
			query.must(QueryBuilders.matchPhrasePrefixQuery("keyword", keyword ));
		}
		if(isOpen != null) {//0公开 1非公开
			query.must(QueryBuilders.termsQuery("isOpen", isOpen==0?true:false ));
		}
		searchSourceBuilder.query(query).trackTotalHits(true);
		
		//排序
		if(StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(sortOrder)) {
			searchSourceBuilder.sort(sort, sortOrder.equals("DESC")?SortOrder.DESC:SortOrder.ASC);
		}else {
			searchSourceBuilder.sort("endTime.keyword", SortOrder.DESC);//时间倒序
		}
		// 分页
		searchSourceBuilder.from((page - 1) * rows);
		searchSourceBuilder.size(rows);
		// 加入请求体
		searchRequest.source(searchSourceBuilder);
		// 发送请求
		SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
		//获取查询总数量
		SearchRequest request = new SearchRequest(DatacenterEnum.KEYWORKFREQUENCY.getValue()).source(new SearchSourceBuilder().query(query).trackTotalHits(true));
		long count = elasticsearchClient.search(request, RequestOptions.DEFAULT).getHits().getTotalHits().value;
		// 获取搜索的文档结果
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		List<KeywordFrequency> list = new ArrayList<KeywordFrequency>();
		for (int i = 0; i < hits.length; i++) {
			SearchHit hit = hits[i];
			String json = hit.getSourceAsString();// 将文档中的每一个对象转换json串值
			KeywordFrequency info = Y9JacksonUtil.readValue(json, KeywordFrequency.class);
			list.add(info);
		}
		long countAll = elasticsearchClient.search(new SearchRequest(DatacenterEnum.KEYWORKFREQUENCY.getValue()).source(new SearchSourceBuilder().trackTotalHits(true)), RequestOptions.DEFAULT).getHits().getTotalHits().value;
		dataMap.put("countAll", countAll);
		dataMap.put("pages", (count + rows - 1) / rows);
		dataMap.put("total", count);
		dataMap.put("dataList", list);
		return dataMap;
	}

	@Override
	public void delFrequency(String[] ids) {
		for(String id : ids) {
			KeywordFrequency kf = keywordFrequencyRepository.findById(id).orElse(null);
			if(kf != null) {
				keywordFrequencyRepository.delete(kf);
			}
		}
	}

	@Override
	public void disabledFrequency(String[] ids) {
		for(String id : ids) {
			KeywordFrequency kf = keywordFrequencyRepository.findById(id).orElse(null);
			if(kf != null) {
				kf.setIsOpen(!kf.getIsOpen());
				keywordFrequencyRepository.save(kf);
			}
		}
	}

	@Override
	public void exportFrequency(HttpServletResponse response, HttpServletRequest request) throws IOException {
		SearchRequest searchRequest = new SearchRequest(DatacenterEnum.KEYWORKFREQUENCY.getValue()).searchType(SearchType.DFS_QUERY_THEN_FETCH);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.sort("frequency", SortOrder.DESC);
		searchSourceBuilder.size(10000);
		searchRequest.source(searchSourceBuilder);
		SearchResponse rp = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
		SearchHits searchHits = rp.getHits();
		SearchHit[] hits = searchHits.getHits();
		String data = "[";
		for (int i = 0; i < hits.length; i++) {
			SearchHit hit = hits[i];
			String json = hit.getSourceAsString();// 将文档中的每一个对象转换json串值
			KeywordFrequency info = Y9JacksonUtil.readValue(json, KeywordFrequency.class);
			data = data + "{\"搜索词\":\""+info.getKeyword()+"\",\"词频\":\""+info.getFrequency()+"\",\"最后更新时间\":\""+info.getEndTime()+"\"}," ;
		}
    	data += "]";
		try {
			exportExcel("词频.xlsx", data, net.risesoft.controller.dto.Frequency.class, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
     * 数据导出
     * @param fileName 导出excel名称
     * @param data 导出的数据
     * @param c 导出数据的实体class
     * @param response 响应
     * @throws Exception
     */
    @SuppressWarnings("resource")
	public void exportExcel(String fileName, String data, Class<?> c, HttpServletResponse response) throws Exception {
        try {
            // 创建表头
            // 创建工作薄
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();
            // 创建表头行
            Row rowHeader = sheet.createRow(0);
            if (c == null) {
                throw new RuntimeException("Class对象不能为空!");
            }
            Field[] declaredFields = c.getDeclaredFields();
            List<String> headerList = new ArrayList<>();
            if (declaredFields.length == 0) {
                return;
            }
            for (int i = 0; i < declaredFields.length; i++) {
                Cell cell = rowHeader.createCell(i, CellType.STRING);
                String headerName = String.valueOf(declaredFields[i].getName());
                cell.setCellValue(headerName);
                headerList.add(i, headerName);
            }
            // 填充数据
            List<?> objects = JSONObject.parseArray(data, c);
            //Object obj = c.newInstance();
            if (!CollectionUtils.isEmpty(objects)) {
                for (int o = 0; o < objects.size(); o++) {
                    Row rowData = sheet.createRow(o + 1);
                    for (int i = 0; i < headerList.size(); i++) {
                        Cell cell = rowData.createCell(i);
                        Field nameField = c.getDeclaredField(headerList.get(i));
                        nameField.setAccessible(true);
                        String value = String.valueOf(nameField.get(objects.get(o)));
                        cell.setCellValue(value);
                    }
                }
            }
            response.setContentType("application/vnd.ms-excel");
            String resultFileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + resultFileName + ";" + "filename*=utf-8''" + resultFileName);
            workbook.write(response.getOutputStream());
            workbook.close();
            response.flushBuffer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
}
