package com.bidizhaobiao.tablestore.utils.tablestore.start;

import com.bidizhaobiao.tablestore.nat.entity.Config;
import com.bidizhaobiao.tablestore.nat.utils.TablestoreUtils;
import org.junit.Assert;
import org.junit.Test;
import java.io.InputStream;
import java.util.*;

/**
 * 类描述
 *
 * @author dunhanson
 * @version 1.0.0
 * @since 2023-09-26
 */
public class ConfigTest {

    public InputStream getInputStream() {
        return Config.class.getClassLoader().getResourceAsStream("tablestore.yml");
    }

    @Test
    public void testInitByConfigs() {
        Assert.assertTrue(true);
        List<Config> configs = TablestoreUtils.getConfigs(getInputStream());
        TablestoreUtils.init(configs);
    }

    @Test
    public void testInitByPath() {
        Assert.assertTrue(true);
        String path = "D:\\code\\gitlab\\idea\\tablestore-utils\\src\\test\\resources\\tablestore.yml";
        TablestoreUtils.init(path);
    }

    @Test
    public void testInit() {
        Assert.assertTrue(true);
        TablestoreUtils.init();
    }

    @Test
    public void testInitInputStream() {
        Assert.assertTrue(true);
        TablestoreUtils.init(getInputStream());
    }
}
