package com.hz.show.service;

import com.alibaba.fastjson.JSONObject;
import com.hz.show.dao.InspectionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author liyj
 * @Description
 * @createTime 2020/12/30 15:24
 **/
@Service
public class InspectionServiceImpl implements IInspectionService {

    @Autowired
    private InspectionDao inspectionDao;

    @Value("${file_path}")
    private String filePath;

    @Override
    public JSONObject getInspection(Map<String,Object> map) {
        JSONObject inspection = inspectionDao.getInspection(map);
        filePath = filePath + inspection.get("hotel_name") + "/image/";
        int inspectionId =  inspection.getInteger("id");
        List<JSONObject> inspectionContentList = inspectionDao.getInspectionContentList(inspectionId);
        JSONObject result = new JSONObject();
        result.put("header", inspection);
        result.put("body", inspectionContentList);
        File file = new File(filePath);
        String[] list = file.list();
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                String imageUrls = "";
                File imgDir = new File(filePath + list[i]);
                if (imgDir.isDirectory()) {
                    String[] imgUrl = imgDir.list();
                    for (int j = 0; j < imgUrl.length; j++) {
                        imageUrls += filePath + imgUrl[j] + ";";
                    }
                    result.put(list[i], imageUrls);
                }
            }
        }
        return result;
    }


//    @Override
//    public JSONObject getInspectionHeader(int hotelId) {
//        return inspectionDao.getInspection(hotelId);
//    }

    @Override
    public List<JSONObject> getHotelList() {
        return inspectionDao.getHotelList();
    }

    @Override
    public List<JSONObject> getPropertyList(int hotelId) {
        return inspectionDao.getPropertyList(hotelId);
    }

    @Override
    public List<JSONObject> getInspectionList(Map<String, Object> map) {
        return inspectionDao.getInspectionList(map);
    }


    @Override
    public void deleteInspection(int hotelId) {
        inspectionDao.deleteInspection(hotelId);
    }

    @Override
    public void deleteInspectionContent(int inspectionId) {
        inspectionDao.deleteInspectionContent(inspectionId);
    }

    @Override
    public void addInspectionHistory(int hotelId) {
          inspectionDao.addInspectionHistory(hotelId);
    }

    @Override
    public void addInspectionContentHistory(int inspectionId) {
        inspectionDao.addInspectionContentHistory(inspectionId);
    }

    @Override
    public void deleteProperty(int hotelId) {
        inspectionDao.deleteProperty(hotelId);
    }

    @Override
    public List<JSONObject> getMaxProblemHotelTop10() {
        return inspectionDao.getMaxProblemHotelTop10();
    }

    @Override
    public List<JSONObject> getMaxProblemTop10() {
        return inspectionDao.getMaxProblemTop10();
    }

    @Override
    public List<JSONObject> getMaxTrainingContentTop5() {
        return inspectionDao.getMaxTrainingContentTop5();
    }

    @Override
    public List<JSONObject> getServiceAppraise() {
        return inspectionDao.getServiceAppraise();
    }

    @Override
    public List<JSONObject> getInspectionHotel() {
        return inspectionDao.getInspectionHotel();
    }


}
