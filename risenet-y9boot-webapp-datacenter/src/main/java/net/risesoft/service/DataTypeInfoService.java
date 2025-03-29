package net.risesoft.service;

import java.util.List;

import org.springframework.stereotype.Service;

import net.risesoft.nosql.elastic.entity.DataTypeInfo;

@Service
public interface DataTypeInfoService {
	
	List<DataTypeInfo> getDataTypeAll();

	List<DataTypeInfo> getDataTypeByIsShow(Boolean isShow);
	
	Boolean typeSwitch(String id);
	
	DataTypeInfo getDataType(String dataName);
	
	String[] getDataTypeIndexs();

}
