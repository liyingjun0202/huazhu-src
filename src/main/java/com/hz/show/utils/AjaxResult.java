package com.hz.show.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * 操作消息提醒
 *
 * @author cheng
 */
public class AjaxResult extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;

    public static final String CODE_TAG = "status";

    public static final String MSG_TAG = "message";

    public static final String DATA_TAG = "data";

    private JSONObject loginfo = new JSONObject();

    public JSONObject getLoginfo() {
        return loginfo;
    }

    public void setLoginfo(JSONObject loginfo) {
        this.loginfo = loginfo;
    }




    /**
     * 状态类型
     */
    public enum Type
    {
        /** 成功 */
        SUCCESS(200),
        /** 警告 */
        WARN(301),
        /** 错误 */
        ERROR(500);
        private final int value;

        Type(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public AjaxResult()
    {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param type 状态类型
     * @param msg 返回内容
     */
    public AjaxResult(Type type, String msg)
    {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param type 状态类型
     * @param msg 返回内容
     * @param data 数据对象
     */
    public AjaxResult(Type type, String msg, Object data)
    {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data))
        {
            super.put(DATA_TAG, data);
        }
    }

    public AjaxResult(int code, String msg)
    {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult success()
    {
        //return AjaxResult.success("操作成功");
        return new AjaxResult(Type.SUCCESS, "操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static AjaxResult success(Object data)
    {
        return AjaxResult.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static AjaxResult success(String msg)
    {
        return AjaxResult.success(msg, null);
    }
//    /**
//     * 返回成功消息
//     *
//     * @param msg 返回内容
//     * @param data 数据对象
//     * @return 成功消息
//     */
//    public static AjaxResult success(String msg)
//    {
//        return new AjaxResult(Type.SUCCESS, msg);
//    }
    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(String msg, Object data)
    {
        return new AjaxResult(Type.SUCCESS, msg, data);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult warn(String msg)
    {
        return AjaxResult.warn(msg, null);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult warn(String msg, Object data)
    {
        return new AjaxResult(Type.WARN, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static AjaxResult error()
    {
        // return AjaxResult.error("操作失败");
        return new AjaxResult(Type.ERROR, "操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult error(String msg)
    {
        return AjaxResult.error(msg, null);

    }
    /**
     * 返回错误消息
     * @param code 返回内容
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult error(int code, String msg)
    {
        return new AjaxResult(code,msg);

    }
    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult error(String msg, Object data)
    {
        return new AjaxResult(Type.ERROR, msg, data);
    }




    /**
     * 返回错误消息
     * @param code 返回内容
     * @param msg 返回内容
     * @return 警告消息
     */
    public static String failed(int code, String msg) {
        return JSON.toJSONString(new AjaxResult(code, msg));
    }

}
