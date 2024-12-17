package net.risesoft.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserUsageLogDTO {
	
	/**
	 * 用户名称
	 */
	private String userName;
	
	/**
	 * 客户端IP
	 */
	private String userHostIp;
	
	/**
	 * 模块名称
	 */
	private String modularName;
	
	/**
	 * 操作方法
	 */
	private String methodName;
	
	/**
	 * 操作名称
	 */
	private String operateName;
	
	/**
	 * 操作类型
	 */
	private String operateType;
	
	/**
	 * 操作状态
	 */
	private String success;

	/**
	 * 日志级别
	 */
	private String logLevel;
	
	/**
	 * 操作用时
	 */
	private String elapsedTime;
	
	/**
	 * 操作时间(yyyy-MM-dd HH:mm:ss)
	 */
	private String logTime;
	
	/**
	 * 参数
	 */
	private String parameter;
	
	/**
	 * 筛选时间(yyyy-MM-dd)
	 */
	private String startDate; 
	private String endDate;

}
