package net.risesoft.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jodconverter.JodConverter;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;

import com.aspose.words.Document;
import com.aspose.words.NodeType;
import com.aspose.words.SaveFormat;

import net.risesoft.y9.Y9Context;

public class FileConversionUtil {

    /**
     * office文件转换成pdf或html文件
     *
     *
     * @param file         要转换文件的路径，要包含名称和后缀（如：D:\GoogleDownload\中国地理复习笔记归纳总结(特细).doc）
     * @param saveFilePath 转换完后文件的保存路径(如：F:/test)
     * @param finalType    最终要转换为的文件的后缀传pdfh或html （如：pdf）
     *
     * @return             返回最后转换后的文件名
     * @throws             IOException
     */
//    public static String conversionPdfOrHtml(String file, String saveFilePath, String finalType) throws IOException {
//        FileInputStream inputStream = null;
//        try {
//            inputStream = new FileInputStream(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        String type = file.substring(file.lastIndexOf(".") + 1);
//        return conversionPdfOrHtml(inputStream, saveFilePath, type, finalType);
//    }
    
    /**
     * office文件转换成pdf或html文件
     *
     *
     * @param file         要转换文件的路径，要包含名称和后缀（如：D:\GoogleDownload\中国地理复习笔记归纳总结(特细).doc）
     * @param saveFilePath 转换完后文件的保存路径(如：F:/test)
     * @param finalType    最终要转换为的文件的后缀传pdfh或html （如：pdf）
     *
     * @return             返回最后转换后的文件名
     * @throws             IOException
     */
    public static String conversionPdfOrHtml(String file,InputStream inputStream, String saveFilePath, String finalType) throws IOException {
    	String filename=file.substring(0,file.lastIndexOf("."));
    	String type = file.substring(file.lastIndexOf(".") + 1);
    	return conversionPdfOrHtml(inputStream, saveFilePath, filename, type, finalType);
    }


    /**
     * office文件转换成pdf或html文件
     *
     *
     * @param fromFileInputStream 要转换文件的文件流
     * @param saveFilePath        转换完后文件的保存路径(如：F:/test)
     * @param fileType            原始文件的后缀（如：doc）
     * @param finalType           最终要转换为的文件的后缀 （如：pdf）
     *
     * @return                    返回最后转换后的文件名
     * @throws                    IOException
     */
    public static String conversionPdfOrHtml(InputStream fromFileInputStream, String saveFilePath, String fileName, String fileType, String finalType) throws IOException {
        String docFileName = null;
        String resultFileName = null;
        finalType = "." + finalType;
        //识别文件类型
        if ("doc".equals(fileType)) {
            docFileName = fileName + ".doc";
            resultFileName = fileName + finalType;
        } else if ("docx".equals(fileType)) {
            docFileName = fileName + ".docx";
            resultFileName = fileName + finalType;
        } else if ("xlsx".equals(fileType)) {
            docFileName = fileName + ".xlsx";
            resultFileName = fileName + finalType;
        } else if ("xls".equals(fileType)) {
            docFileName = fileName + ".xls";
            resultFileName = fileName + finalType;
        } else if ("ppt".equals(fileType)) {
            docFileName = fileName + ".ppt";
            resultFileName = fileName + finalType;
        } else if ("pptx".equals(fileType)) {
            docFileName = fileName + ".pptx";
            resultFileName = fileName + finalType;
        } else if ("txt".equals(fileType)) {
            docFileName = fileName + ".txt";
            resultFileName = fileName + finalType;
        } else if ("wps".equals(fileType)) {
            docFileName = fileName + ".doc";
            resultFileName = fileName + finalType;
        } else if ("ofd".equals(fileType)) {
            docFileName = fileName + ".ofd";
            resultFileName = fileName + finalType;
        } else if ("jpg".equals(fileType)) {
            docFileName = fileName + ".jpg";
            resultFileName = fileName + finalType;
        } else if ("png".equals(fileType)) {
            docFileName = fileName + ".png";
            resultFileName = fileName + finalType;
        } else if ("tif".equals(fileType)) {
            docFileName = fileName + ".tif";
            resultFileName = fileName + finalType;
        } else {
            return "转换错误，此文件格式无法转换";
        }
        //如果saveFilePath路径下有同名的原始文件和转化后的文件则删除。然后在saveFilePath下创建空的原始文件和转化后的文件
        File resultOutputFile = new File(saveFilePath + File.separatorChar + resultFileName);
        File docInputFile = new File(saveFilePath + File.separatorChar + docFileName);
        //File resultOutputFile = new File("C:\\Users\\YCJ\\Desktop\\" + resultFileName);
        //File docInputFile = new File("C:\\Users\\YCJ\\Desktop\\" + docFileName);
        if (resultOutputFile.exists()) {
            resultOutputFile.delete();
        }
        if (docInputFile.exists()) {
            docInputFile.delete();
        }
        resultOutputFile.createNewFile();
        docInputFile.createNewFile();
        //将待转文件拷贝一份写入到saveFilePath下创建的空原始文件里
        OutputStream os = null;
        try {
            os = new FileOutputStream(docInputFile);
            int bytesRead = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            
        } catch (IOException e) {
        } finally {
        	if(os != null) {
        		try {
        			os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	fromFileInputStream.close();
        }

        System.out.println("转换中......");
        
    	if ("doc".equals(fileType) || "docx".equals(fileType)) {
			try {
				Document doc = new Document(saveFilePath + File.separatorChar + docFileName);
				doc.acceptAllRevisions();
		        doc.getChildNodes(NodeType.COMMENT, true).clear();//清除所有注释
		        doc.getChildNodes(NodeType.COMMENT_RANGE_START, true).clear();//清除注释区域的开头
		        doc.getChildNodes(NodeType.COMMENT_RANGE_END, true).clear();//清除注释区域的结尾
				doc.save(saveFilePath + File.separatorChar + resultFileName, SaveFormat.PDF);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("doc/docx 转换失败");
				resultOutputFile.delete();
			}
    		
    		//off2pdf(saveFilePath + File.separatorChar + docFileName,saveFilePath);
    		docInputFile.delete();
            return resultFileName;
    	}
    	
    	String officePath = Y9Context.getProperty("y9.common.openOfficePath");
    	LocalOfficeManager officeManager = null;
    	try {
    		
    		officeManager = LocalOfficeManager.builder().officeHome(officePath).install().build();
    		// 判断openoffice服务是否打开
            if(!officeManager.isRunning()) officeManager.start();
            
			JodConverter.convert(docInputFile).to(resultOutputFile).execute();
		} catch (OfficeException e) {
			e.printStackTrace();
		} finally {
			try {
				if(officeManager != null && officeManager.isRunning()) {
					officeManager.stop();
				}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //转换完之后删除拷贝的原始文件
        docInputFile.delete();
        return resultFileName;
        
    }

}
