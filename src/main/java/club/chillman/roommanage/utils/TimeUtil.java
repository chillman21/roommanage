package club.chillman.roommanage.utils;



import club.chillman.roommanage.exception.RoomManageException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author NIU
 * @createTime 2021/4/18 1:13
 */
public class TimeUtil {
    private static final SimpleDateFormat DATE_SDF = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat NORMAL_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static String getDateSdf(String date) {
        try {
            Date normalDate = NORMAL_SDF.parse(date);
            return DATE_SDF.format(normalDate);
        } catch (ParseException e) {
            throw new RoomManageException(404, "格式转换错误" + e.getMessage());
        }

    }

    public static Date str2Date(String dateTime){
        try {
            return NORMAL_SDF.parse(dateTime);
        } catch (ParseException e) {
            throw new RoomManageException(404, "格式转换错误" + e.getMessage());
        }
    }

    public static String getDate(String time) {
        try {
            Date normalDate = NORMAL_SDF.parse(time);
            return DATE.format(normalDate);
        } catch (ParseException e) {
            throw new RoomManageException(404, "格式转换错误" + e.getMessage());
        }

    }
}
