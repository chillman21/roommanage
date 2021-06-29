package club.chillman.roommanage.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author NIU
 * @createTime 2021/4/23 17:28
 */
@Component
public class MetaHandler implements MetaObjectHandler {
    /**
     * 新增数据执行
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        boolean hasSetter = metaObject.hasSetter("gmtCreate");
        if (hasSetter) {
            this.setFieldValByName("gmtCreate", new Date(), metaObject);
            this.setFieldValByName("gmtModified", new Date(), metaObject);
        }
    }

    /**
     * 更新数据执行
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Object val = getFieldValByName("gmtModified", metaObject);
        if (val == null) {
            this.setFieldValByName("gmtModified", new Date(), metaObject);
        }
    }
}
