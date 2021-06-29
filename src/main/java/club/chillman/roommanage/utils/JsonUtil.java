package club.chillman.roommanage.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author NIU
 * @createTime 2021/3/30 20:58
 */
public enum JsonUtil {
    ;

    /**
     * 将JSON字符串转为Java对象
     */
    public static <T> T toJavaObject(String result, Class<T> clazz) {
        return JSONObject.toJavaObject(JSONObject.parseObject(result), clazz);
    }

    /**
     * JSON字符串对象解析成java List对象
     */
    public static <T> List<T> toJavaList(String resultList, Class<T> clazz) {
        return JSONArray.parseArray(resultList).toJavaList(clazz);
    }

}
