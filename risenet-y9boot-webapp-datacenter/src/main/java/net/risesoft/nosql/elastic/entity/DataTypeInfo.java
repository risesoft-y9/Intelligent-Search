package net.risesoft.nosql.elastic.entity;

import java.io.Serializable;
import java.util.List;

import net.risesoft.nosql.elastic.entity.Article.FreeField;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据类型
 * 
 */
@Document(indexName = "data_type")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DataTypeInfo  implements Serializable{
	
	private static final long serialVersionUID = 3861050192416789928L;

	/**
	 * 主键
	 */
	@Id
	@Field(type = FieldType.Keyword)
	private String id;
	
	/**
	 * 数据类型Name(例: 收文)
	 */
	@Field(type = FieldType.Keyword)
	private String dataName;

	/**
	 * 是否给用户显示该类型数据
	 */
	@Field(type = FieldType.Keyword)
	private Boolean isShow = true;

	/**
	 * 其他字段
	 */
	@Field(type = FieldType.Nested)
	private List<FreeField> freeFields;
	
	/**
	 * 顺序
	 */
	@Field(type = FieldType.Integer)
	private Integer index=1;
	
}
