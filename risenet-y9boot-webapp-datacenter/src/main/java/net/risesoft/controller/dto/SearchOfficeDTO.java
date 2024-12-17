package net.risesoft.controller.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchOfficeDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6166025204738418156L;

	/**
     * 搜索类型(1为精准查询,0为智能查询)
     */
    private String type;
	
	/**
     * 搜索内容
     */
    private String searchContent;
    
	/**
     * 类型列表(例: 收文,局发文)
     */
	private String dataType;
    
    /**
     * 时间筛选(格式: yyyy-MM-dd - yyyy-MM-dd)
     */
    private String timeType;
    
    /**
     * 排序(时间正序、时间倒序、热度排序)
     */
    private String sortStr;
    
    /** 分页 */
    private Integer page;

    private Integer rows;
    
    
}
