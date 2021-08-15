package top.getawaycar.user.common.authorization.configuration;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import top.getawaycar.user.common.authorization.pojo.constant.DataStatusEnum;

@Slf4j
@Component
public class MybatisMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 起始版本 3.3.0(推荐使用)
        Long currentTime = System.currentTimeMillis();
        this.strictInsertFill(metaObject, "createTime", Long.class, currentTime);
        this.strictInsertFill(metaObject, "dataStatus", Integer.class, DataStatusEnum.ENABLE.getCode());
        this.strictInsertFill(metaObject, "dataStatus", Long.class,Long.valueOf( DataStatusEnum.ENABLE.getCode()));
    }


    @Override
    public void updateFill(MetaObject metaObject) {
        // 起始版本 3.3.0(推荐使用)
        this.strictUpdateFill(metaObject, "lastUpdateTime", Long.class, System.currentTimeMillis());
    }
}
