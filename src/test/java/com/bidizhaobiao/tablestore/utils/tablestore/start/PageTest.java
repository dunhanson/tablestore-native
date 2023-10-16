package com.bidizhaobiao.tablestore.utils.tablestore.start;

import com.bidizhaobiao.tablestore.nat.entity.Page;
import org.junit.Assert;
import org.junit.Test;

/**
 * 类描述
 *
 * @author dunhanson
 * @version 1.0.0
 * @since 2023-09-27
 */
public class PageTest {
    @Test
    public void test() {
        Assert.assertTrue(true);
        System.out.println(new Page<>());
        System.out.println(new Page<>(1, 30));
    }
}
