package net.risesoft.controller.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DataLogDTO  implements Serializable {

	private static final long serialVersionUID = -2992568584002492961L;

	/**
	 * 主键
	 */
	private String id;
	
	/**
	 * 文章类型
	 */
	private String dataType;
	
    /**
     * 标题
     */
    private String title;

	/**
	 * 数据时间
	 */
	private String dataTime;

	/**
	 * 导入时间(yyyy-MM-dd HH:mm:ss)
	 */
    private String leadInTime;
    
	/**
	 * 异常状态
	 */
	private String error;
	
	/**
	 * 是否公开,默认公开
	 */
	private Boolean isOpen = true;
	
}
