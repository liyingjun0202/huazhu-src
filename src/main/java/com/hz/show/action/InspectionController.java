package com.hz.show.action;

import com.alibaba.fastjson.JSONObject;
import com.hz.show.domain.User;
import com.hz.show.service.IInspectionService;
import com.hz.show.service.IUserService;
import com.hz.show.utils.AjaxResult;
import com.hz.show.utils.ExcelUtil;
import com.hz.show.utils.FileUtil;
import com.hz.show.utils.WordUtil;
import com.hz.show.utils.page.TableDataInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author liyj
 * @Description
 * @createTime 2020/12/30 13:45
 **/
@RestController
@RequestMapping("/inspection")
public class InspectionController extends BaseController {

    @Autowired
    private IInspectionService inspectionService;

    @Autowired
    private IUserService userService;

    /**
     * @description 获取酒店巡检详情
     * @param hotelId
     * @throws 
     * @author liyj 
     * @updateTime 2021/1/4  
     */
    @GetMapping("/getInspection")
    @ApiOperation(value = "获取酒店巡检详情")
    public AjaxResult getInspection(@ApiParam(name="hotelId",value="酒店ID")@RequestParam(required = false) Integer hotelId,@ApiParam(name="inspectionId",value="巡检ID") @RequestParam(required = false) Integer inspectionId) {
        Map<String,Object> map  = new HashMap<>();
        map.put("hotel_id",hotelId);
        map.put("inspection_id",inspectionId);
        JSONObject inspection = inspectionService.getInspection(map);
        return AjaxResult.success(inspection);
    }


    /**
     * @description 上传文件夹 
     * @param file
 * @param request
     * @throws 
     * @author liyj 
     * @updateTime 2021/1/4  
     */
    @PostMapping("/upload")
    @ApiOperation(value = "上传文件夹")
    public void upload(@ApiParam("file") @RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        String relativePath = request.getParameter("relativePath");
        System.out.println(relativePath);
        if (file.getOriginalFilename().endsWith("docx")) {
            WordUtil.readWord(file);
        } else if (file.getOriginalFilename().toLowerCase().endsWith("jpg") ||
                file.getOriginalFilename().toLowerCase().endsWith("png") ||
                file.getOriginalFilename().toLowerCase().endsWith("mp4") ||
                file.getOriginalFilename().toLowerCase().endsWith("pdf")) {
                FileUtil.moveFile(file,relativePath);
        } else if(file.getOriginalFilename().toLowerCase().endsWith("xlsx")) {
            ExcelUtil.importExcelToData(file);
        }
    }

    /**
     * @description 获取酒店列表
     * @param 
     * @throws 
     * @author liyj 
     * @updateTime 2021/1/4  
     */
    @GetMapping("/getHotelList")
    @ApiOperation(value = "获取酒店列表")
    public TableDataInfo getHotelList(@RequestParam(required = false)  @ApiParam(value = "pageSize",required = false) Integer pageSize,
                                      @RequestParam(required = false)  @ApiParam(value = "pageNum" ,required = false) Integer pageNum) {
        startPage();
        return  getDataTable(inspectionService.getHotelList());
    }

    /**
     * @description 获取酒店资产信息
     * @param hotelId
     * @throws 
     * @author liyj 
     * @updateTime 2021/1/4  
     */
    @GetMapping("/getPropertyList")
    @ApiOperation(value = "获取酒店资产信息")
    public AjaxResult getPropertyList(@ApiParam(name="hotelId",value = "酒店ID") Integer hotelId) {
        return AjaxResult.success(inspectionService.getPropertyList(hotelId));
    }

    /**
     * @description  获取巡店列表
     * @param
     * @throws
     * @author liyj
     * @updateTime 2021/1/4
     */
    @GetMapping("/getInspectionList")
    @ApiOperation(value = "获取巡店列表")
    public TableDataInfo getInspectionList(@RequestParam(required = false)  @ApiParam(value = "pageSize",required = false) Integer pageSize,
                                           @RequestParam(required = false)  @ApiParam(value = "pageNum" ,required = false) Integer pageNum) {
        startPage();
        Map<String,Object> map =new HashMap<String,Object>();
        return getDataTable(inspectionService.getInspectionList(map));
    }


    /**
     * @description  获取用户列表
     * @param
     * @throws
     * @author liyj
     * @updateTime 2021/1/4
     */
    @GetMapping("/getUserList")
    @ApiOperation(value = "获取用户列表")
    public TableDataInfo getUserList(@RequestParam(required = false)  @ApiParam(value = "pageSize",required = false) Integer pageSize,
                                     @RequestParam(required = false)  @ApiParam(value = "pageNum" ,required = false) Integer pageNum) {
        startPage();
        Map<String,Object> map =new HashMap<String,Object>();
        return getDataTable(userService.getUserList(map));
    }


    /**
     * @description 登录
     * @throws
     * @author liyj 
     * @updateTime 2021/1/5  
     */
    @PostMapping("/validateUser")
    @ApiOperation(value = "登录")
    public AjaxResult validateUser(@ApiParam(name="user",value="用户信息(账号userName,密码password)") @RequestBody User user,
                                   HttpServletRequest request) {
        User result = userService.getUser(user);
        if(result != null ) {
            result.setExpiredTime(System.currentTimeMillis());
            String token = UUID.randomUUID().toString();
            result.setToken(token);
            result.setPassword(null);
            request.getSession().setAttribute(result.getUserName(),result);
            //request.getSession().setAttribute("token-"+result.getUserName(),token);
            return AjaxResult.success(result);
        } else {
            return AjaxResult.warn("login fail");
        }
    }

    @PostMapping("/updateUser")
    @ApiOperation(value = "修改用户")
    public AjaxResult updateUser(@ApiParam(name="user",value="用户信息") @RequestBody User user
                               ) {
        userService.updateUser(user);
        return AjaxResult.success();
    }


    @PostMapping("/addUser")
    @ApiOperation(value = "新增用户")
    public AjaxResult addUser(@ApiParam(name="user",value="用户信息") @RequestBody User user) {
        userService.addUser(user);
        return AjaxResult.success();
    }



    @DeleteMapping("/deleteUsers")
    @ApiOperation(value = "删除用户")
    public AjaxResult deleteUsers(@ApiParam(name="ids",value="用户ID数组") String[] ids) {
        userService.deleteUsers(ids);
        return AjaxResult.success();
    }


    @GetMapping("/getMaxProblemHotelTop10")
    @ApiOperation(value = "获取巡检问题最多的十个门店")
    public AjaxResult getMaxProblemHotelTop10() {
        List<JSONObject> list = inspectionService.getMaxProblemHotelTop10();
        return AjaxResult.success(list);
    }

    @GetMapping("/getMaxProblemTop10")
    @ApiOperation(value = "获取巡检问题Top10")
    public AjaxResult getMaxProblemTop10() {
        List<JSONObject> list = inspectionService.getMaxProblemTop10();
        return AjaxResult.success(list);
    }

    @GetMapping("/getMaxTrainingContentTop5")
    @ApiOperation(value = "获取最希望培训的内容Top5")
    public AjaxResult getMaxTrainingContentTop5() {
        List<JSONObject> list = inspectionService.getMaxTrainingContentTop5();
        return AjaxResult.success(list);
    }

    @GetMapping("/getServiceAppraise")
    @ApiOperation(value = "获取服务评价")
    public AjaxResult getServiceAppraise() {
        List<JSONObject> list = inspectionService.getServiceAppraise();
        return AjaxResult.success(list);
    }

    @GetMapping("/getInspectionHotel")
    @ApiOperation(value = "获取服务评价")
    public AjaxResult getInspectionHotel() {
        List<JSONObject> list = inspectionService.getInspectionHotel();
        return AjaxResult.success(list);
    }






}
