package org.springframework.data.mybatis.support;

/**
 * Created by will on 8/17/17.
 * 默认系统用户以10000开始, 后面递增
 */
public class GuidHolder {
    public static String getGuid(){
        return "10000";
    }
}
