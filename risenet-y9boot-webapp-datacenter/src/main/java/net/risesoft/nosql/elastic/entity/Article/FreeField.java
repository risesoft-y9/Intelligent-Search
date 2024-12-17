package net.risesoft.nosql.elastic.entity.Article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * 字段信息
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FreeField implements Serializable {

	private static final long serialVersionUID = 4314783534168311487L;

	/**
	 * 字段名称
	 */
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String name;

	/**
	 * 字段名称(中文)
	 */
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String nameCN;

	/**
	 * 搜索类型(Keyword/Text)
	 */
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String searchType;

	/**
	 * 字段内容(Keyword)
	 */
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String content;
	
	/**
	 * 字段内容(Text)
	 */
	@Field(type = FieldType.Text, index = true, store = true, searchAnalyzer = "ik_smart", analyzer = "ik_smart")
	private String content4Text;
	
	public FreeField(String name, String nameCN, String searchType) {
		this.name = name;
		this.nameCN = nameCN;
		this.searchType = searchType;
	}

	public FreeField(String name, String content) {
		this.name = name;
		this.nameCN = name;
		this.searchType = "Keyword";
		this.content = content;
		this.content4Text = content;
	}

	public FreeField(String name) {
		this.name = name;
		this.nameCN = name;
		this.searchType = "Keyword";
	}
	
}
