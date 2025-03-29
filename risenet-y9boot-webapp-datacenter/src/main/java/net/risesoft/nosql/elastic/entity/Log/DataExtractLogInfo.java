package net.risesoft.nosql.elastic.entity.Log;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据抽取日志
 * 
 */
@Document(indexName = "data_extracting_log")
@NoArgsConstructor
@Data
public class DataExtractLogInfo implements Serializable{

	private static final long serialVersionUID = 1643879302182267109L;
	
	/**
	 * 主键
	 */
	@Id
	@Field(type = FieldType.Keyword)
	private String id;
	
	/**
	 * 文章类型
	 */
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String dataType;
	
	/**
	 * 标题
	 */
	@Field(type = FieldType.Text, index = true, store = true, searchAnalyzer = "ik_smart", analyzer = "ik_smart")
	private String title;

	/**
	 * 数据时间(yyyy-MM-dd)
	 */
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String dataTime;

	/**
	 * 导入时间(yyyy-MM-dd HH:mm:ss)
	 */
	@Field(type = FieldType.Date , pattern = "yyyy-MM-dd HH:mm:ss", format = DateFormat.custom, store = true)
	private Date leadInTime;

	/**
	 * 异常状态
	 */
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String error;
	
	/**
	 * 是否公开,默认公开
	 */
	@Field(type = FieldType.Boolean, index = true, store = true)
	private Boolean isOpen = true;
	
}
