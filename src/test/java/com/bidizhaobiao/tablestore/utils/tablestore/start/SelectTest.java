package com.bidizhaobiao.tablestore.utils.tablestore.start;

import com.alicloud.openservices.tablestore.model.search.query.QueryBuilders;
import com.alicloud.openservices.tablestore.model.search.query.TermsQuery;
import com.bidizhaobiao.tablestore.nat.entity.Condition;
import com.bidizhaobiao.tablestore.nat.entity.Order;
import com.bidizhaobiao.tablestore.nat.entity.Sort;
import com.bidizhaobiao.tablestore.nat.utils.TablestoreUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
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
public class SelectTest {

    @Test
    public void testSelectOne() {
        // 实例
        String instanceName = "bxkc-ots";
        // 表名
        String tableName = "document";
        // 索引
        String indexName = "document_index";
        // 查询 Query
        TermsQuery query = QueryBuilders.terms("docid").terms(332733258L).build();
        // 排序
        List<Sort> sorts = Collections.singletonList(new Sort("docid", Order.DESC));
        // 返回字段
        List<String> columns = Arrays.asList("docid", "doctitle");
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
