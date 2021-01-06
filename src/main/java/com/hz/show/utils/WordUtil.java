package com.hz.show.utils;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hz.show.dao.InspectionDao;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;


/**
 * @author liyj
 * @Description
 * @createTime 2020/12/28 15:33
 **/
@Component
public class WordUtil {


    private static WordUtil fileUtils;

    @Autowired
    private InspectionDao inspectionDao;

    @PostConstruct
    public void init() {
        fileUtils = this;
        fileUtils.inspectionDao = this.inspectionDao;
    }

    public static void main(String[] args) {
        //doc文档路径
        String filePath = "C:\\Users\\liyingjun\\Desktop\\华住\\酒店.docx";
        //test.print(filePath,"第一个表");
        //    FileUtils.testWord(filePath);

//        String excelPath = "D:\\华住资料\\常州金坛美居酒店\\2130222-海友常州新北万达酒店-信息收集.xlsx";
//
//        FileUtils.importExcelToData(excelPath);
    }


    public static void readWord(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            //载入文档 //如果是office2007  docx格式
            if (file.getOriginalFilename().toLowerCase().endsWith("docx")) {
                //word 2007 图片不会被读取， 表格中的数据会被放在字符串的最后
                XWPFDocument xwpf = new XWPFDocument(inputStream);//得到word文档的信息
//             List<XWPFParagraph> listParagraphs = xwpf.getParagraphs();//得到段落信息
                Iterator<XWPFTable> it = xwpf.getTablesIterator();//得到word中的表格

                while (it.hasNext()) {

                    XWPFTable table = it.next();
                    List<XWPFTableRow> rows = table.getRows();
                    //读取每一行数据
                    JSONObject inspectionHead = new JSONObject();
//                    for(int i = 0 ; i < rows.size() ; i ++ ) {
//                        XWPFTableRow row = rows.get(i);
//                        for(int j = 0 ; j < row.getTableCells().size(); j ++  ) {
//                            System.out.println(i+"==="+j+"===="+row.getTableCells().get(j).getText());
//                        }
//                    }
                    for (int i = 1; i < 6; i++) {
                        XWPFTableRow row = rows.get(i);
                        //读取每一列数据
                        List<XWPFTableCell> cells = row.getTableCells();
                        for (int j = 0; j < cells.size(); j++) {
                            XWPFTableCell cell = cells.get(j);
                            //System.out.println(i+"="+j+"==="+cell.getText());
                            //输出当前的单元格的数据
                            if (i == 2 && j == 1) {
                                //客房数
                                inspectionHead.put("room_number", cell.getText());
                            } else if (i == 2 && j == 3) {
                                //ip
                                inspectionHead.put("ip", cell.getText());
                            } else if (i == 2 && j == 5) {
                                //联系电话
                                inspectionHead.put("phone", cell.getText());
                            } else if (i == 3 && j == 1) {
                                //酒店ID
                                inspectionHead.put("hotel_id", cell.getText());
                            } else if (i == 3 && j == 3) {
                                //酒店名称
                                inspectionHead.put("hotel_name", cell.getText());
                            } else if (i == 3 && j == 5) {
                                //工单ID
                                inspectionHead.put("order_id", cell.getText());
                            } else if (i == 4 && j == 1) {
                                //巡店日期
                                inspectionHead.put("date", cell.getText());
                            } else if (i == 4 && j == 3) {
                                //巡店人
                                inspectionHead.put("people", cell.getText());
                            } else if (i == 4 && j == 5) {
                                //店长
                                inspectionHead.put("shop_manager", cell.getText());
                            } else if (i == 5 && j == 1) {
                                //酒店地址
                                inspectionHead.put("hotel_address", cell.getText());
                            }
                        }
                    }
                    Map<String,Object> map = new HashMap<>();
                    map.put("hotel_id",inspectionHead.getString("hotel_id"));
                    //获取上次的巡检信息
                    JSONObject inspectionResult = fileUtils.inspectionDao.getInspection(map);
                    //如果没有历史巡检信息直接保存 如果有则需要将历史巡检信息保存到历史表
                    if(inspectionResult != null ) {
                         Integer historyInspectionId = inspectionResult.getInteger("id");
                        //保存上次巡检信息到历史表
                        fileUtils.inspectionDao.addInspectionHistory(inspectionHead.getInteger("hotel_id"));
                        fileUtils.inspectionDao.addInspectionContentHistory(historyInspectionId);
                        //删除上次巡检信息
                        fileUtils.inspectionDao.deleteInspection(inspectionHead.getInteger("hotel_id"));
                        fileUtils.inspectionDao.deleteInspectionContent(historyInspectionId);
                    }

                    //保存巡检头信息
                    fileUtils.inspectionDao.addInspection(inspectionHead);
                    String inspectionId = inspectionHead.get("id").toString();
                    for (int i = 8; i < rows.size(); i++) {
                        JSONObject content = new JSONObject();
                        //保存前台系统环境检查内容
                        if (i > 7 && i < 22) {
                            content = new JSONObject();
                            content.put("inspection_id", inspectionId);
                            content.put("inspection_type", rows.get(6).getCell(0).getText());
                            content.put("inspection_content", rows.get(8).getCell(1).getText());
                            content.put("inspection_one", "");
                            content.put("inspection_two", "");
                            content.put("inspection_three", "");
                            content.put("inspection_project", rows.get(i).getCell(2).getText());
                            content.put("inspection_condition", rows.get(i).getCell(3).getText());
                            content.put("modify_content", rows.get(i).getCell(4).getText());
                        } else if (i > 21 && i < 33) {
                            //保存酒店网络环境检查
                            content = new JSONObject();
                            content.put("inspection_id", inspectionId);
                            content.put("inspection_type", rows.get(6).getCell(0).getText());
                            content.put("inspection_content", rows.get(22).getCell(1).getText());
                            if (i < 26) {
                                content.put("inspection_one", rows.get(22).getCell(2).getText());
                                content.put("inspection_two", rows.get(22).getCell(3).getText());
                                content.put("inspection_project", rows.get(i).getCell(4).getText());
                                content.put("inspection_condition", rows.get(i).getCell(5).getText());
                                content.put("modify_content", rows.get(i).getCell(6).getText());
                            } else if (i > 25 && i < 32) {
                                content.put("inspection_one", rows.get(26).getCell(2).getText());
                                content.put("inspection_project", rows.get(i).getCell(3).getText());
                                content.put("inspection_condition", rows.get(i).getCell(4).getText());
                                content.put("modify_content", rows.get(i).getCell(5).getText());
                            } else {
                                content.put("inspection_one", rows.get(32).getCell(2).getText());
                                content.put("inspection_project", rows.get(i).getCell(3).getText());
                                content.put("inspection_condition", rows.get(i).getCell(4).getText());
                                content.put("modify_content", rows.get(i).getCell(5).getText());
                            }

                        } else if (i > 32 && i < 44) {
                            //保存酒店机房环境检查
                            content = new JSONObject();
                            content.put("inspection_id", inspectionId);
                            content.put("inspection_type", rows.get(6).getCell(0).getText());
                            content.put("inspection_content", rows.get(33).getCell(1).getText());
                            if (i < 42) {
                                content.put("inspection_one", rows.get(33).getCell(2).getText());
                                content.put("inspection_project", rows.get(i).getCell(3).getText());
                                content.put("inspection_condition", rows.get(i).getCell(4).getText());
                                content.put("modify_content", rows.get(i).getCell(5).getText());
                            } else {
                                content.put("inspection_one", rows.get(42).getCell(2).getText());
                                content.put("inspection_project", rows.get(i).getCell(3).getText());
                                content.put("inspection_condition", rows.get(i).getCell(4).getText());
                                content.put("modify_content", rows.get(i).getCell(5).getText());
                            }
                        } else if (i > 43 && i < 49) {
                            //保存电话系统内容
                            content = new JSONObject();
                            content.put("inspection_id", inspectionId);
                            content.put("inspection_type", rows.get(6).getCell(0).getText());
                            content.put("inspection_content", rows.get(44).getCell(1).getText());
                            if (i < 46) {
                                content.put("inspection_one", rows.get(44).getCell(2).getText());
                                content.put("inspection_project", rows.get(i).getCell(3).getText());
                                content.put("inspection_condition", rows.get(i).getCell(4).getText());
                                content.put("modify_content", rows.get(i).getCell(5).getText());
                            } else if (i > 45 && i < 49) {
                                content.put("inspection_one", rows.get(46).getCell(2).getText());
                                content.put("inspection_project", rows.get(i).getCell(3).getText());
                                content.put("inspection_condition", rows.get(i).getCell(4).getText());
                                content.put("modify_content", rows.get(i).getCell(5).getText());
                            } else {
                                content.put("inspection_one", rows.get(49).getCell(2).getText());
                                content.put("inspection_project", rows.get(i).getCell(3).getText());
                                content.put("inspection_condition", rows.get(i).getCell(4).getText());
                                content.put("modify_content", rows.get(i).getCell(5).getText());
                            }
                        } else if (i > 49 && i < 53) {
                            //保存公共区内容
                            content = new JSONObject();
                            content.put("inspection_id", inspectionId);
                            content.put("inspection_type", rows.get(6).getCell(0).getText());
                            content.put("inspection_content", rows.get(50).getCell(1).getText());
                            content.put("inspection_one", rows.get(i).getCell(2).getText());
                            content.put("inspection_project", rows.get(i).getCell(3).getText());
                            content.put("inspection_condition", rows.get(i).getCell(4).getText());
                            content.put("modify_content", rows.get(i).getCell(5).getText());
                        } else if (i == 53) {
                            //保存监控系统内容
                            content = new JSONObject();
                            content.put("inspection_id", inspectionId);
                            content.put("inspection_type", rows.get(6).getCell(0).getText());
                            content.put("inspection_content", rows.get(53).getCell(1).getText());
                            content.put("inspection_one", rows.get(i).getCell(2).getText());
                            content.put("inspection_project", rows.get(i).getCell(3).getText());
                            content.put("inspection_condition", rows.get(i).getCell(4).getText());
                            content.put("modify_content", rows.get(i).getCell(5).getText());
                        } else if (i > 53 & i < 59) {
                            //保存基础设施及应用检查
                            content = new JSONObject();
                            content.put("inspection_id", inspectionId);
                            content.put("inspection_type", rows.get(6).getCell(0).getText());
                            content.put("inspection_content", rows.get(54).getCell(1).getText());
                            content.put("inspection_one", rows.get(i).getCell(2).getText());
                            content.put("inspection_project", rows.get(i).getCell(3).getText());
                            content.put("inspection_condition", rows.get(i).getCell(4).getText());
                            content.put("modify_content", rows.get(i).getCell(5).getText());
                        } else if (i > 58 & i< 61) {
                            //保存基础设施及应用检查
                            content = new JSONObject();
                            content.put("inspection_id", inspectionId);
                            content.put("inspection_type", rows.get(6).getCell(0).getText());
                            content.put("inspection_content", rows.get(59).getCell(1).getText());
                            content.put("inspection_one", rows.get(59).getCell(2).getText());
                            content.put("inspection_project", rows.get(i).getCell(3).getText());
                            content.put("inspection_condition", rows.get(i).getCell(4).getText());
                            content.put("modify_content", rows.get(i).getCell(5).getText());
                        } else if (i == 62) {
                            //门店其他问题收集及处理记录
                            content = new JSONObject();
                            content.put("inspection_id", inspectionId);
                            content.put("inspection_type", rows.get(61).getCell(0).getText());
                            content.put("modify_content", rows.get(i).getCell(0).getText());
                        } else if (i == 64) {
                            //门店对于IT服务的需求及建议
                            content = new JSONObject();
                            content.put("inspection_id", inspectionId);
                            content.put("inspection_type", rows.get(63).getCell(0).getText());
                            content.put("modify_content", rows.get(i).getCell(0).getText());
                        } else if (i == 66) {
                            //门店对于相关系统的需求及建议
                            content = new JSONObject();
                            content.put("inspection_id", inspectionId);
                            content.put("inspection_type", rows.get(65).getCell(0).getText());
                            content.put("modify_content", rows.get(i).getCell(0).getText());
                        } else if (i > 70 && i < 80) {
                            //门店希望IT组织的培训内容
                            content = new JSONObject();
                            content.put("inspection_id", inspectionId);
                            content.put("inspection_type", rows.get(69).getCell(0).getText());
                            content.put("inspection_one", 5);
                            content.put("inspection_content", rows.get(i).getCell(1).getText());
                            content.put("modify_content", rows.get(i).getCell(2).getText());
                        } else if (i == 81) {
                            //店长对此次巡店服务的评介和建议
                            content = new JSONObject();
                            content.put("inspection_id", inspectionId);
                            content.put("inspection_one", 6);
                            content.put("inspection_type", rows.get(80).getCell(0).getText());
                            content.put("modify_content", rows.get(i).getCell(0).getText().split("☐")[1]);
                        }
                        if (!content.isEmpty()) {
                            System.out.println(content.toString());
                            fileUtils.inspectionDao.addInspectionContent(content);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
