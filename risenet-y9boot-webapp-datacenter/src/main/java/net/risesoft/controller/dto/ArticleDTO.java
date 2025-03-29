package net.risesoft.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.risesoft.nosql.elastic.entity.Article.FreeField;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleDTO  implements Serializable {

    private static final long serialVersionUID = -2992568584002492966L;

    /**
     * 主键
     */
    private String id;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 标题
     */
    private String title;

    /**
     * 主要内容
     */
    private String content;

    /**
     * 数据时间(yyyy-MM-dd)
     */
    private String dataTime;

    /**
     * 导入时间(yyyy-MM-dd)
     */
    private String leadInTime;

    /**
     * 查看次数
     */
    private Integer clickNum;

    /**
     * 租户Id
     */
    private String tenantId;

    /**
     * 其他字段
     */
    private List<FreeFieldDTO> freeFields;

    /**
     * 附件
     */
    private List<AttachmentDTO> attachments;

}
