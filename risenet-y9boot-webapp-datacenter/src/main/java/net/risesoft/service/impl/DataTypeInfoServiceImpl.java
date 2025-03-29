package net.risesoft.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.risesoft.nosql.elastic.entity.DataTypeInfo;
import net.risesoft.nosql.elastic.repository.DataTypeInfoRepository;
import net.risesoft.service.DataTypeInfoService;

@Service(value = "dataTypeInfoService")
@Transactional(readOnly = true)
public class DataTypeInfoServiceImpl implements DataTypeInfoService{

	@Autowired
	private DataTypeInfoRepository dataTypeInfoRepository;

	@Override
	public List<DataTypeInfo> getDataTypeAll() {
		Iterable<DataTypeInfo> iterable = dataTypeInfoRepository.findAll();
		List<DataTypeInfo> list = new ArrayList<>();
		iterable.forEach(single ->{list.add(single);});
		return list;
	}

	@Override
	public List<DataTypeInfo> getDataTypeByIsShow(Boolean isShow) {
		return dataTypeInfoRepository.findByIsShowOrderByIndex(isShow);
	}

	@Override
	public Boolean typeSwitch(String id) {
		DataTypeInfo info = dataTypeInfoRepository.findById(id).orElse(null);
		if(info == null) return false;
		info.setIsShow(!info.getIsShow());
		dataTypeInfoRepository.save(info);
		return true;
	}

	@Override
	public DataTypeInfo getDataType(String dataName) {
		DataTypeInfo info = dataTypeInfoRepository.findByDataName(dataName);
		return info;
	}
	
	@Override
	public String[] getDataTypeIndexs() {
		List<DataTypeInfo> list = dataTypeInfoRepository.findByIsShowOrderByIndex(true);
		List<String> indexList = list.stream().map(DataTypeInfo::getDataName).collect(Collectors.toList());
		return indexList.toArray(new String[indexList.size()]);
	}
	
}
