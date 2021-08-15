package top.getawaycar.user.common.authorization.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>Title: QueryWrapperUtils</p>
 * <p>Description: QueryWrapperUtils QueryWrapperFactory类,负责建立QueryWrapper</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/05
 */
public class QueryWrapperUtils {

    private static final String DATA_STATUS_COLUMN = "data_status";
    private static final Integer VISIBLE_DATA_STATUS = 1;

    private static final String SORT_DATA_COLUMN = "data_sort";
    private static final String CREATE_TIME_COLUMN = "create_time";


    public static <T> QueryWrapper<T> getQueryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DATA_STATUS_COLUMN, VISIBLE_DATA_STATUS);
        return queryWrapper;
    }

    public static <T> QueryWrapper<T> getQueryWrapper(Long id) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq(DATA_STATUS_COLUMN, VISIBLE_DATA_STATUS);
        return queryWrapper;
    }

    public static <T> QueryWrapper<T> getDESCSortedQueryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DATA_STATUS_COLUMN, VISIBLE_DATA_STATUS);
        queryWrapper.orderByDesc(SORT_DATA_COLUMN);
        return queryWrapper;
    }

    public static <T> QueryWrapper<T> getASCSortedQueryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DATA_STATUS_COLUMN, VISIBLE_DATA_STATUS);
        queryWrapper.orderByAsc(SORT_DATA_COLUMN);
        return queryWrapper;
    }

    public static <T> QueryWrapper<T> getCreateTimeDESCSortedQueryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DATA_STATUS_COLUMN, VISIBLE_DATA_STATUS);
        queryWrapper.orderByDesc(CREATE_TIME_COLUMN);
        return queryWrapper;
    }

    public static <T> QueryWrapper<T> getCreateTimeASCCSortedQueryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DATA_STATUS_COLUMN, VISIBLE_DATA_STATUS);
        queryWrapper.orderByAsc(CREATE_TIME_COLUMN);
        return queryWrapper;
    }

}
