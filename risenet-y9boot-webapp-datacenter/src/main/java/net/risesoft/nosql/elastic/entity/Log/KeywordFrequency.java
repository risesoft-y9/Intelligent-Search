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

@Document(indexName = "keyword_frequency")
@NoArgsConstructor
@Data
public class KeywordFrequency implements Serializable{
	
	private static final long serialVersionUID = 2864514534274258947L;
	
	/**
	 * 主键
	 */
	@Id
	@Field(type = FieldType.Keyword)
	private String id;
	
	/**
	 * 关键字
	 */
	@Field(type = FieldType.Text, index = true, store = true, searchAnalyzer = "ik_smart", analyzer = "ik_smart")
	private String keyword;
	
	/**
	 * 频率
	 */
	@Field(type = FieldType.Integer)
	private Integer frequency;
	
	/**
	 * 最后修改时间(yyyy-MM-dd HH:mm:ss)
	 */
	@Field(type = FieldType.Date , pattern = "yyyy-MM-dd HH:mm:ss", format = DateFormat.custom, store = true)
	private Date endTime;
	
	/**
     * 是否公开,默认公开
     */
    @Field(type = FieldType.Boolean, index = true, store = true)
    private Boolean isOpen = true;

}
