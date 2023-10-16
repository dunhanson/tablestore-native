package com.bidizhaobiao.tablestore.nat.entity;

import com.alicloud.openservices.tablestore.model.search.query.Query;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

/**
 * 查询条件
 * @author dunhanson
 * @since 2023-04-14
 * @version 1.0.0
 */
@Data
@Builder
@Slf4j
public class Condition {
    /**
     * 示例名称
     */
    private String instanceName;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 索引名称
     */
    private String indexName;
    /**
     * 主键
     */
    private String primaryKey;
    /**
     * 返回字段
     */
    private List<String> columns;
    /**
     * 排序
     */
    private List<Sort> sorts;
    /**
     * Query
     */
    private Query query;
    /**
     * 当前页
     */
    private Integer pageNo;
    /**
     * 分页大小
     */
    private Integer pageSize;
    /**
     * 获取所有记录
     */
    private Boolean allRecords;
}
