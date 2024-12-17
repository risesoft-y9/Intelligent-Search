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

@Document(indexName = "user_usage_log")
@NoArgsConstructor
@Data
public class UserUsageLog implements Serializable{
	
	private static final long serialVersionUID = -2980308294036082923L;
	
	/**
	 * 主键
	 */
	@Id
	@Field(type = FieldType.Keyword)
	private String id;
	
	/**
	 * 用户id
	 */
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String userId;
	
	/**
	 * 用户名称
	 */
	@Field(type = FieldType.Text, index = true, store = true, searchAnalyzer = "ik_smart", analyzer = "ik_smart")
	private String userName;
	
	/**
	 * 客户端IP
	 */
	@Field(type = FieldType.Text, index = true, store = true, searchAnalyzer = "ik_smart", analyzer = "ik_smart")
	private String userHostIp;
	
	/**
	 * 模块名称
	 */
	@Field(type = FieldType.Text, index = true, store = true, searchAnalyzer = "ik_smart", analyzer = "ik_smart")
	private String modularName;
	
	/**
	 * 操作方法
	 */
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String methodName;
	
	/**
	 * 操作名称
	 */
	@Field(type = FieldType.Text, index = true, store = true, searchAnalyzer = "ik_smart", analyzer = "ik_smart")
	private String operateName;
	
	/**
	 * 操作类型
	 */
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String operateType;
	
	/**
	 * 操作状态
	 */
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String success;

	/**
	 * 日志级别
	 */
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String logLevel;
	
	/**
	 * 操作用时
	 */
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String elapsedTime;
	
	/**
	 * 操作时间(yyyy-MM-dd HH:mm:ss)
	 */
	@Field(type = FieldType.Date , pattern = "yyyy-MM-dd HH:mm:ss", format = DateFormat.custom, store = true )
	private Date logTime;
	
	/**
	 * 参数
	 */
	@Field(type = FieldType.Text, index = true, store = true, searchAnalyzer = "ik_smart", analyzer = "ik_smart")
	private String parameter;
	
	/**
     * 租户id
     */
    @Field(type = FieldType.Keyword, store = true)
    private String tenantId;

}
