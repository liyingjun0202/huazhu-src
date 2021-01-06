package com.hz.show.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author liyj
 * @Description
 * @createTime 2020/12/30 11:30
 **/
@Mapper
public interface InspectionDao {

    public int  addInspection(Map<String,Object> param);

    public void addInspectionContent(Map<String,Object> map);

    public JSONObject getInspection(Map<String,Object> map);

    public List<JSONObject> getInspectionList(Map<String,Object> map);

    public List<JSONObject> getInspectionContentList(int inspectionId);

    public void addHotelProperty(Map<String,Object> param);

    public List<JSONObject> getHotelList();

    public List<JSONObject> getHotel(int hotelId);

    public List<JSONObject> getPropertyList(int hotelId);

    public List<JSONObject> getUserList(Map<String,Object> map);

    public void deleteInspection(int hotelId);

    public void deleteInspectionContent(int inspectionId);

    public void addInspectionHistory(int hotelId);

    public void addInspectionContentHistory(int inspectionId);

    public void deleteProperty(int hotelId);

    public JSONObject getUser(Map<String,Object> map);

    public List<JSONObject> getMaxProblemHotelTop10();

    public List<JSONObject> getMaxProblemTop10();

    public List<JSONObject> getMaxTrainingContentTop5();

    public List<JSONObject> getServiceAppraise();

    public List<JSONObject> getInspectionHotel();

}
