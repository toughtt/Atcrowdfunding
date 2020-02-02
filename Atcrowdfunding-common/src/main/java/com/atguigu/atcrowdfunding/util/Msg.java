package com.atguigu.atcrowdfunding.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wall
 * @data 20 - 15:01
 */

    public class Msg {
            private String i;
            private String j;
            private String k;
            private String l;




                //    定义状态吗100-成功，200-失败
                private int code;
                //    提示信息
                private String message;

                public String getMessage() {
                    return message;
                }

                public void setMessage(String message) {
                    this.message = message;
                }

                //    用户要返给浏览器的数据
                private Map<String,Object> extend = new HashMap<>();
                public static Msg success(){
                    Msg result = new Msg();
            result.setCode(100);
            result.setMessage("处理成功");
            return result;
        }
        public static Msg fail(){
            Msg result = new Msg();
            result.setCode(200);
            result.setMessage("处理失败");
            return result;
        }
        public Msg add(String key,Object value){
            this.getExtend().put(key,value);
            return this;

        }


        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }


        public Map<String, Object> getExtend() {
            return extend;
        }

        public void setExtend(Map<String, Object> extend) {
            this.extend = extend;
        }
    }


