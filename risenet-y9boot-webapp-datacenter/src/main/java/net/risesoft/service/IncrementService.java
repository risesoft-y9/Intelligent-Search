package net.risesoft.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public interface IncrementService {

	void importDate(MultipartFile xlsxFile, HttpServletResponse response) throws IOException;

}
