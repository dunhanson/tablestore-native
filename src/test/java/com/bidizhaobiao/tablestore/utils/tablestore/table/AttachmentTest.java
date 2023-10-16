package com.bidizhaobiao.tablestore.utils.tablestore.table;


import com.alicloud.openservices.tablestore.model.search.query.Query;
import com.alicloud.openservices.tablestore.model.search.query.QueryBuilders;
import com.bidizhaobiao.tablestore.nat.entity.Condition;
import com.bidizhaobiao.tablestore.nat.entity.Sort;
import com.bidizhaobiao.tablestore.nat.utils.TablestoreUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 类描述
 *
 * @author dunhanson
 * @version 1.0.0
 * @since 2023-09-26
 */
public class AttachmentTest {

    @Test
    public void test() {
        // 实例
        String instanceName = "bxkc-ots";
        // 表名
        String tableName = "attachment";
        // 索引
        String indexName = "attachment_index";
        // 查询 Query
        Query query = QueryBuilders.wildcard("path", "*1654958589151.pdf").build();
        // 返回字段
        List<String> columns = Collections.singletonList("path");
        // 排序
        List<Sort> sorts = Collections.emptyList();
        // 条件 Condition
        Condition condition = Condition.builder()
                .instanceName(instanceName)
                .tableName(tableName)
                .indexName(indexName)
                .columns(columns)
                .sorts(sorts)
                .query(query)
                .pageNo(1)
                .pageSize(30)
                .build();
        Map<String, Object> map = TablestoreUtils.selectOne(condition);
        Assert.assertNotNull(map);
        System.out.println(map);
    }
}
