package com.hz.show.utils;

import com.alibaba.fastjson.JSONObject;
import com.hz.show.dao.InspectionDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liyj
 * @Description
 * @createTime 2020/12/31 16:08
 **/
@Component
public class ExcelUtil {

    private static ExcelUtil excelUtil;

    @Autowired
    private InspectionDao inspectionDao;

    @PostConstruct
    public void init() {
        excelUtil = this;
        excelUtil.inspectionDao = this.inspectionDao;
    }

     static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static void importExcelToData(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            //根据指定的文件输入流导入Excel从而产生Workbook对象
            Workbook wb0 = new XSSFWorkbook(inputStream);
//            if (file.getOriginalFilename().toLowerCase().indexOf(".xlsx") != -1) {
//                wb0 = new XSSFWorkbook(fileIn);
//            }
//            else {
//                HSSFWorkbook wb = new HSSFWorkbook(fileIn);
//            }
            //获取Excel文档中的第一个表单
            Sheet sht0 = wb0.getSheetAt(0);
            //对Sheet中的每一行进行迭代
            List list = new ArrayList();
            sht0.getRow(0).getCell(1).setCellType(CellType.STRING);
            String hotelId = sht0.getRow(0).getCell(1).getStringCellValue();
            String hotelName = sht0.getRow(1).getCell(1).getStringCellValue();
            String pattern = sht0.getRow(2).getCell(1).getStringCellValue();
            sht0.getRow(3).getCell(1).setCellType(CellType.STRING);
            String orderId =  sht0.getRow(3).getCell(1).getStringCellValue() ;
            Date date  = sht0.getRow(4).getCell(1).getDateCellValue();
            //先把上次的资产信息删除
            excelUtil.inspectionDao.deleteProperty(Integer.valueOf(hotelId));
            for(int i = 0 ; i < sht0.getLastRowNum() ; i ++ ) {
                Row r = sht0.getRow(i);
                r.getCell(0).setCellType(CellType.STRING);
                r.getCell(1).setCellType(CellType.STRING);
                JSONObject params = new JSONObject();
                params.put("hotel_id",hotelId);
                params.put("hotel_name",hotelName);
                params.put("order_id",orderId);
                params.put("date",date);
                params.put("pattern",pattern);
                params.put("name",sht0.getRow(i).getCell(0).getStringCellValue());
                params.put("val",sht0.getRow(i).getCell(1).getStringCellValue());
                if(r.getRowNum() > 5 && r.getRowNum() < 19)  {
                    params.put("type",sht0.getRow(5).getCell(0).getStringCellValue());
                } else  if(r.getRowNum() > 18 && r.getRowNum() < 24) {
                    params.put("type",sht0.getRow(18).getCell(0).getStringCellValue());
                } else  if(r.getRowNum() > 24 && r.getRowNum() < 27) {
                    params.put("type",sht0.getRow(24).getCell(0).getStringCellValue());
                } else  if(r.getRowNum() > 27 && r.getRowNum() < 29) {
                    params.put("type",sht0.getRow(27).getCell(0).getStringCellValue());
                } else  if(r.getRowNum() > 29 && r.getRowNum() < 39) {
                    params.put("type",sht0.getRow(29).getCell(0).getStringCellValue());
                } else  if(r.getRowNum() > 39 && r.getRowNum() < 47) {
                    params.put("type",sht0.getRow(47).getCell(0).getStringCellValue());
                } else  if(r.getRowNum() > 47 && r.getRowNum() < 67) {
                    params.put("type",sht0.getRow(47).getCell(0).getStringCellValue());
                } else  if(r.getRowNum() > 67 && r.getRowNum() < 72) {
                    params.put("type",sht0.getRow(67).getCell(0).getStringCellValue());
                } else  if(r.getRowNum() > 72 && r.getRowNum() < 80) {
                    params.put("type",sht0.getRow(72).getCell(0).getStringCellValue());
                } else  if(r.getRowNum() > 80 && r.getRowNum() < 97) {
                    params.put("type",sht0.getRow(80).getCell(0).getStringCellValue());
                } else  if(r.getRowNum() > 97 && r.getRowNum() < 121) {
                    params.put("type",sht0.getRow(97).getCell(0).getStringCellValue());
                }
                //如果当前行是表头行 不进行操作
                if(params.get("type") != null ) {
                    excelUtil.inspectionDao.addHotelProperty(params);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
