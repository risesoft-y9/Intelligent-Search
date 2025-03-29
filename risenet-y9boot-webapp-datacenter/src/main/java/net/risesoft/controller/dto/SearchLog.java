package net.risesoft.controller.dto;

import lombok.Data;

@Data
public class SearchLog {
	
	//id
	private String id;
	
	//标题
	private String title;
	
	//是否公开
	private String isOpen;
	
	//数据类型(多数据类型用 , 隔开)
	private String dataName;

	
}
