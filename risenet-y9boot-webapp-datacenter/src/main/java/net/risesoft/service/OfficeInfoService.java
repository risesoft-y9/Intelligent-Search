package net.risesoft.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.risesoft.controller.dto.ArticleDTO;
import net.risesoft.controller.dto.SearchOfficeDTO;
import org.springframework.stereotype.Service;

@Service
public interface OfficeInfoService {

	/**
	 * 全文检索
	 * 
	 * @param searchOfficeDTO
	 * @return
	 */
	Map<String, Object> searchOfficeInfo(SearchOfficeDTO searchOfficeDTO);
	
	/**
	 * 根据标题搜索
	 * @param keyword
	 * @return
	 * @throws IOException 
	 */
	List<String> searchTitleResult(String keyword) throws IOException;
	
	/**
	 * 根据标题搜索
	 * @param keyword
	 * @param dataType
	 * @param id
	 * @return
	 */
	Map<String, Object> searchTitleResult(String keyword, String dataType, String id);
	
	/**
	 * 打开显示文档
	 * 
	 * @param id
	 * @return
	 */
	ArticleDTO textdisplay(String id, String dataType, String keyword);

	/**
	 * 文件下载
	 * 
	 * @param url
	 * @param response
	 * @param request
	 */
	void download(String url, String fileName, HttpServletResponse response, HttpServletRequest request);

}
