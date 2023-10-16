package com.bidizhaobiao.tablestore.utils.tablestore.table;

import com.alicloud.openservices.tablestore.model.Row;
import com.alicloud.openservices.tablestore.model.search.query.Query;
import com.bidizhaobiao.tablestore.nat.entity.Condition;
import com.bidizhaobiao.tablestore.nat.utils.TablestoreUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 类描述
 *
 * @author dunhanson
 * @version 1.0.0
 * @since 2023-09-27
 */
@Slf4j
public class TablestoreTest {
    @Test
    public void test() {
        Assert.assertTrue(true);
    }

    @Test
    public void testCreatePrimaryKeyQuery() {
        Assert.assertTrue(true);
        Query query = TablestoreUtils.createPrimaryKeyQuery("docid", 364457336L);
        log.info("query:{}", query);
    }

    @Test
    public void testSelectPrimaryKeyList() {
        Assert.assertTrue(true);
        Condition condition = Condition.builder().build();
        List<Object> objects = TablestoreUtils.selectPrimaryKeyList(condition);
        log.info("objects:{}", objects);
    }

    @Test
    public void testToMaps() {
        Assert.assertTrue(true);
        List<Row> rows = new ArrayList<>();
        List<Map<String, Object>> maps = TablestoreUtils.toMaps(rows);
        log.info("maps:{}", maps);
    }
}
