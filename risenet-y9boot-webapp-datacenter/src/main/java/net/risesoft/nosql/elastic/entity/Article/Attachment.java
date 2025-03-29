package net.risesoft.nosql.elastic.entity.Article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Map;

/**
 * 文件信息
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachment implements Serializable {

	private static final long serialVersionUID = 4314783534168301887L;

	/**
	 * 附件ID
	 */
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String fileguid;

	/**
	 * 附件名称
	 */
	@Field(type = FieldType.Text, index = true, store = true, searchAnalyzer = "ik_smart", analyzer = "ik_smart")
	private String fileName;

	/**
	 * 附件类型
	 */
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String fileType;

	/**
	 * 附件URL
	 */
	@Field(type = FieldType.Text)
	private String fileUrl;

	/**
	 * 附件内容
	 */
	@Field(type = FieldType.Text, index = true, store = true, searchAnalyzer = "ik_smart", analyzer = "ik_smart")
	private String fileContent;
	
	/**
	 * 是否解析附件 默认为false(未解析) 成功调用getfile后变更为true(解析)
	 */
	@Field(type = FieldType.Boolean, index = true, store = true)
	private Boolean parsingFile = false;
	
	/**
	 * 异常状态
	 */
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String error;
	
	private Map<String, Object> fileFrequency;

	public Attachment(String fileName, String fileType, String fileUrl) {
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileUrl = fileUrl;
	}
}
