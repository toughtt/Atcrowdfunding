package com.atguigu.atcrowdfunding.util;

import java.util.HashMap;

/**
 * @author wall
 * @data 20 - 15:46
 */
public class AjaxRusult {
    private String message;
    private boolean success;
    private Page page;
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    private HashMap<String,Object> map =new HashMap<>();

    public HashMap<String, Object> getMap() {
        return map;
    }

    public void setMap(HashMap<String, Object> map) {
        this.map = map;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
