package net.risesoft.service.impl;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.risesoft.nosql.elastic.entity.DataTypeInfo;
import net.risesoft.nosql.elastic.entity.Log.DataExtractLogInfo;
import net.risesoft.nosql.elastic.repository.DataTypeInfoRepository;
import net.risesoft.service.DataTypeInfoService;
import net.risesoft.y9.util.Y9Guid;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.risesoft.nosql.elastic.entity.Article.FreeField;
import net.risesoft.nosql.elastic.entity.Article.Attachment;
import net.risesoft.nosql.elastic.entity.Article.ArticleInfo;
import net.risesoft.nosql.elastic.repository.DataExtractLogInfoRepository;
import net.risesoft.nosql.elastic.repository.ArticleInfoRepository;
import net.risesoft.service.IncrementService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Service(value = "incrementService")
@Transactional(readOnly = true)
public class IncrementServiceImpl implements IncrementService{

    @Autowired
    private DataTypeInfoService dataTypeInfoService;

    @Autowired
    private DataTypeInfoRepository dataTypeInfoRepository;
    
    @Autowired
    private ArticleInfoRepository articleInfoRepository;
	
    @Autowired
    private DataExtractLogInfoRepository dataExtractLogInfoRepository;


    @Override
    public void importDate(MultipartFile xlsxFile, HttpServletResponse response) throws IOException {
        String filename = xlsxFile.getOriginalFilename();
        InputStream inputStream = xlsxFile.getInputStream();
        if (filename.endsWith(".xlsx")) {
            readXlsx(inputStream);
        } else if(filename.endsWith(".xls")){
            readXls(inputStream);
        }
    }

    private void readXlsx(InputStream inputStream) throws IOException {
        XSSFWorkbook sheets = new XSSFWorkbook(inputStream);
        //获取第一个工作表
        XSSFSheet sheetAt = sheets.getSheetAt(0);

        //第一列,确定好怎么读取
        XSSFRow xr1 = sheetAt.getRow(0);
        int fileIndex = xr1.getPhysicalNumberOfCells();
        for(int i=4;i<xr1.getPhysicalNumberOfCells();i++){
            if(xr1.getCell(i).getStringCellValue().toString().contains("附件名称")){
                fileIndex = i;
                break;
            }
        }

        addDataType( sheetAt.getRow(1).getCell(0).getStringCellValue(),null,xr1);

        for (int rowNum = 1; rowNum < xr1.getPhysicalNumberOfCells(); rowNum++) {
            XSSFRow row = sheetAt.getRow(rowNum);
            if (row != null) {
                ArticleInfo articleInfo = new ArticleInfo();
                DataExtractLogInfo dataExtractLogInfo = new DataExtractLogInfo();
                String allData = "",error="";
                try{
                    articleInfo.setDataType(row.getCell(0).getStringCellValue());//数据类型
                    dataExtractLogInfo.setDataType(row.getCell(0).getStringCellValue());
                    articleInfo.setTitle(row.getCell(1).getStringCellValue());//标题
                    dataExtractLogInfo.setTitle(row.getCell(1).getStringCellValue());
                    articleInfo.setContent(row.getCell(2).getStringCellValue());//主要内容
                    articleInfo.setDataTime(row.getCell(3).getStringCellValue());//数据时间
                    dataExtractLogInfo.setDataTime(row.getCell(3).getStringCellValue());

                    allData += row.getCell(1).getStringCellValue()  + " " + row.getCell(2).getStringCellValue();

                    List<FreeField> freeFields = new ArrayList<>();
                    for(int i=4;i<fileIndex;i++) {
                        allData += " " + row.getCell(i).getStringCellValue();
                        freeFields.add(new FreeField(xr1.getCell(i).getStringCellValue(),row.getCell(i).getStringCellValue()));
                    }
                    articleInfo.setFreeFields(freeFields);
                    List<Attachment> attachments = new ArrayList<>();
                    for(int i=fileIndex;i<row.getPhysicalNumberOfCells();){
                        allData += " " + row.getCell(i).getStringCellValue();
                        attachments.add(new Attachment(row.getCell(i++).getStringCellValue(),row.getCell(i++).getStringCellValue(),row.getCell(i++).getStringCellValue()));
                    }
                    articleInfo.setAttachments(attachments);
                }catch(Exception e){
                    //捕获异常
                    final Writer result = new StringWriter();
                    final PrintWriter printWriter = new PrintWriter(result);
                    e.fillInStackTrace().printStackTrace(printWriter);
                    //将异常信息转换为字符串输出
                    error = result.toString();
                }

                String uuid = Y9Guid.genGuid();
                articleInfo.setId(uuid);
                dataExtractLogInfo.setId(uuid);
                articleInfo.setLeadInTime(new Date());
                dataExtractLogInfo.setLeadInTime(new Date());
                articleInfo.setAllContent(allData);
                articleInfoRepository.save(articleInfo);
                dataExtractLogInfo.setError(error);
                dataExtractLogInfoRepository.save(dataExtractLogInfo);
            }
        }
    }

    private void readXls(InputStream inputStream) throws IOException {
        HSSFWorkbook sheets = new HSSFWorkbook(inputStream);
        //获得第一个工作表
        HSSFSheet sheetAt = sheets.getSheetAt(0);

        //第一列,确定好怎么读取
        HSSFRow hr1 = sheetAt.getRow(0);
        int fileIndex = hr1.getRowNum()-1;
        for(int i=4;i<hr1.getRowNum();i++){
            if(hr1.getCell(i).getStringCellValue().toString().contains("附件名称")){
                fileIndex = i;
                break;
            }
        }

        System.out.println(hr1.getPhysicalNumberOfCells());
        addDataType( sheetAt.getRow(1).getCell(0).getStringCellValue(),hr1,null);

        for (int rowNum = 1; rowNum < hr1.getPhysicalNumberOfCells(); rowNum++) {
            HSSFRow row = sheetAt.getRow(rowNum);
            if (row != null) {
                ArticleInfo articleInfo = new ArticleInfo();
                DataExtractLogInfo dataExtractLogInfo = new DataExtractLogInfo();
                String allData = "",error="";

                articleInfo.setDataType(row.getCell(0).getStringCellValue());//数据类型
                articleInfo.setTitle(row.getCell(1).getStringCellValue());//标题
                articleInfo.setContent(row.getCell(2).getStringCellValue());//主要内容
                articleInfo.setDataTime(row.getCell(3).getStringCellValue());//数据时间

                allData += row.getCell(1).getStringCellValue() + row.getCell(2).getStringCellValue();

                try{
                    articleInfo.setDataType(row.getCell(0).getStringCellValue());//数据类型
                    dataExtractLogInfo.setDataType(row.getCell(0).getStringCellValue());
                    articleInfo.setTitle(row.getCell(1).getStringCellValue());//标题
                    dataExtractLogInfo.setTitle(row.getCell(1).getStringCellValue());
                    articleInfo.setContent(row.getCell(2).getStringCellValue());//主要内容
                    articleInfo.setDataTime(row.getCell(3).getStringCellValue());//数据时间
                    dataExtractLogInfo.setDataTime(row.getCell(3).getStringCellValue());

                    allData += row.getCell(1).getStringCellValue() + row.getCell(2).getStringCellValue();

                    List<FreeField> freeFields = new ArrayList<>();
                    for(int i=4;i<fileIndex;i++) {
                        allData += row.getCell(i).getStringCellValue();
                        freeFields.add(new FreeField(hr1.getCell(i).getStringCellValue(),row.getCell(i).getStringCellValue()));
                    }
                    articleInfo.setFreeFields(freeFields);
                    List<Attachment> attachments = new ArrayList<>();
                    for(int i=fileIndex;i<row.getPhysicalNumberOfCells();){
                        allData += row.getCell(i).getStringCellValue();
                        attachments.add(new Attachment(row.getCell(i++).getStringCellValue(),row.getCell(i++).getStringCellValue(),row.getCell(i++).getStringCellValue()));
                    }
                    articleInfo.setAttachments(attachments);
                }catch(Exception e){
                    //捕获异常
                    final Writer result = new StringWriter();
                    final PrintWriter printWriter = new PrintWriter(result);
                    e.fillInStackTrace().printStackTrace(printWriter);
                    //将异常信息转换为字符串输出
                    error = result.toString();
                }

                String uuid = Y9Guid.genGuid();
                articleInfo.setId(uuid);
                dataExtractLogInfo.setId(uuid);
                articleInfo.setLeadInTime(new Date());
                dataExtractLogInfo.setLeadInTime(new Date());
                articleInfo.setAllContent(allData);
                articleInfoRepository.save(articleInfo);
                dataExtractLogInfo.setError(error);
                dataExtractLogInfoRepository.save(dataExtractLogInfo);
            }
        }
    }

    private void addDataType(String dataType, HSSFRow hRow, XSSFRow xRow) {
        DataTypeInfo info = dataTypeInfoService.getDataType(dataType);
        if(info != null) return;
        DataTypeInfo dataTypeInfo = new DataTypeInfo();
        dataTypeInfo.setId(dataType);
        dataTypeInfo.setDataName(dataType);
        List<FreeField> freeFields = new ArrayList<>();
        if(hRow != null){
            for(int i=4;i<hRow.getPhysicalNumberOfCells();i++){
                if(hRow.getCell(i).getStringCellValue().toString().contains("附件名称")){
                    break;
                }
                freeFields.add(new FreeField(hRow.getCell(i).getStringCellValue()));
            }
        }
        if (xRow != null){
            for(int i=4;i<xRow.getPhysicalNumberOfCells();i++){
                if(xRow.getCell(i).getStringCellValue().toString().contains("附件名称")){
                    break;
                }
                freeFields.add(new FreeField(xRow.getCell(i).getStringCellValue()));
            }
        }
        dataTypeInfo.setFreeFields(freeFields);
        dataTypeInfoRepository.save(dataTypeInfo);
    }

}
