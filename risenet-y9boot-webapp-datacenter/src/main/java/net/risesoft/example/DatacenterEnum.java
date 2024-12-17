package net.risesoft.example;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DatacenterEnum {
	

	ARTICLE("文章","y9_article_info"),
	DATAEXTRACTINGLOG("日志索引","data_extracting_log"),
	USERUSAGELOG("用户操作索引","user_usage_log"),
	KEYWORKFREQUENCY("搜索词频","keyword_frequency"),
	
	TIMEASC("","时间正序"),
	TIMEDESC("","时间倒序"),
	CLICKNUM("","热度排序");
	
	private final String name;
	private final String value;
	
}
