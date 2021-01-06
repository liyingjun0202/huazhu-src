package com.hz.show.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @author liyj
 * @Description
 * @createTime 2020/12/30 15:23
 **/
public interface IInspectionService {

    public JSONObject getInspection(Map<String,Object> map);

  //  public JSONObject getInspectionHeader(int hotelId);

    public List<JSONObject> getHotelList();

    public List<JSONObject> getPropertyList(int hotelId);

    public List<JSONObject> getInspectionList(Map<String,Object> map);



    public void deleteInspection(int hotelId);

    public void deleteInspectionContent(int inspectionId);

    public void addInspectionHistory(int hotelId);

    public void addInspectionContentHistory(int inspectionId);

    public void deleteProperty(int hotelId);

    public List<JSONObject> getMaxProblemHotelTop10();

    public List<JSONObject> getMaxProblemTop10();

    public List<JSONObject> getMaxTrainingContentTop5();

    public List<JSONObject> getServiceAppraise();

    public List<JSONObject> getInspectionHotel();
}
