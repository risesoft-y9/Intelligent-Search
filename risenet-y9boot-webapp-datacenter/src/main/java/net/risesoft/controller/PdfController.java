package net.risesoft.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import net.risesoft.util.FileConversionUtil;

@Controller
@RequestMapping(value = "/pdf")
@Slf4j
public class PdfController {
	
	@RequestMapping(value = "/index")
	public String index(String id,String fileName, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("fileName", fileName);
		return "pdf/pdfjs";
	}
	
	@SuppressWarnings({ "resource", "unused" })
	@RequestMapping(value = "/localPdf")
	public void getPdf(String id,String fileName,String fileUrl,HttpServletRequest request,HttpServletResponse response) throws IOException {		
		OutputStream out = null;
        try {
        	String name = fileName.substring(0,fileName.lastIndexOf("."));
        	String type = fileName.substring(fileName.lastIndexOf(".") + 1);
            //String path=File.separator+"data"+File.separator+"file"+File.separator+"datacenter"+File.separator+id+File.separator+fileName;
            //String path=File.separator+"data"+File.separator+"file"+File.separator+"datacenter"+File.separator+id;
        	String path="";

        	//name = name.replaceAll("\\+"," ");
        	
        	if("pdf".equals(type)) {
        		path = File.separator+"data"+File.separator+"file"+File.separator+"datacenter"+File.separator+id;
        	}else {
        		path = File.separator+"data"+File.separator+"file"+File.separator+"datacenter2"+File.separator+id;
        	}
        	
        	
            //String path="C:\\Users\\YCJ\\Desktop";
            //response.reset();
        	System.out.println("name: "+name);
        	System.out.println("type: "+type);
        	System.out.println("路径: "+path);
            File file = new File(path+File.separator+name+".pdf");
            if(!file.exists() || file.length() == 0) {
            	if (!file.getParentFile().exists()){
                    //文件夹不存在 生成
                    file.getParentFile().mkdirs();
                }
            	System.out.println("开始创建文件");
                InputStream input = null;
                try {
                	if (fileUrl.contains("&name=")) {
                        String[] str1 = fileUrl.split("&name=");
                        String[] str2 = str1[1].split("&path=");
                        fileUrl = str1[0] + "&name=" + URLEncoder.encode(str2[0], "UTF-8") + "&path=" +str2[1] ;
                    }else if (fileUrl.contains("&fileName=")) {
                    	String[] str = fileUrl.split("&fileName=");
                        fileUrl = str[0] + "&fileName=" + URLEncoder.encode(str[1], "UTF-8");
                    }
                	System.out.println("fileUrl: "+fileUrl);
                	if("pdf".equals(type)) {
                		System.out.println("----------pdf文件----------");
                		//Y9FileDataUtil.getTextFromPdf(fileUrl, fileName, id, false);
                	}else {
                		System.out.println("----------非pdf文件----------");
                		URL url = new URL(fileUrl);
                        URLConnection connection = url.openConnection();
                        input = connection.getInputStream();
                        FileConversionUtil.conversionPdfOrHtml(fileName,input, path, "pdf");
                	}
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (input != null)
                            input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            
            System.out.println("开始读取文件");
            response.setContentType("application/pdf");
            out = response.getOutputStream();
            byte[] buf = new byte[1024];
            InputStream in = new FileInputStream(new File(path+File.separator+name+".pdf"));
            int tempbyte;
            while ((tempbyte = in.read(buf)) != -1) {
                out.write(buf);
            }
            out.flush();
        } catch (IOException e) {
        	log.error("localPdf IOException 错误");
            //e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
	
}
