package net.risesoft.nosql.elastic.entity.Article;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document(indexName = "y9_article_info")
@NoArgsConstructor
@Data
public class ArticleInfo implements Serializable {

    private static final long serialVersionUID = 4311783534168301487L;

    /**
     * 主键
     */
    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    /**
     * 数据类型
     */
    @Field(type = FieldType.Keyword, index = true, store = true)
    private String dataType;

    /**
     * 标题
     */
    @Field(type = FieldType.Text, index = true, store = true, searchAnalyzer = "ik_smart", analyzer = "ik_smart")
    private String title;
    
    /**
     * 主要内容
     */
    @Field(type = FieldType.Text, index = true, store = true, searchAnalyzer = "ik_smart", analyzer = "ik_smart")
    private String content;

    /**
     * 数据时间(yyyy-MM-dd)
     */
    @Field(type = FieldType.Keyword, index = true, store = true)
    private String dataTime;

    /**
     * 导入时间(yyyy-MM-dd)
     */
    @Field(type = FieldType.Date , pattern = "yyyy-MM-dd HH:mm:ss", format = DateFormat.custom, store = true)
    private Date leadInTime;

    /**
     * 查看次数
     */
    @Field(type = FieldType.Integer, index = true, store = true)
    private Integer clickNum=0;

    /**
     * 租户Id
     */
    @Field(type = FieldType.Keyword, index = true, store = true)
    private String tenantId;

    /**
     * 其他字段
     */
    @Field(type = FieldType.Nested)
    private List<FreeField> freeFields;

    /**
     * 附件
     */
    @Field(type = FieldType.Nested)
    private List<Attachment> attachments;

    /**
     * 全部内容
     */
    @Field(type = FieldType.Text, index = true, store = true, searchAnalyzer = "ik_smart", analyzer = "ik_smart")
    private String allContent;

    /**
     * 是否公开,默认公开
     */
    @Field(type = FieldType.Boolean, index = true, store = true)
    private Boolean isOpen = true;

}
