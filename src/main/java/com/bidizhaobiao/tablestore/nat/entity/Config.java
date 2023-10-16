package com.bidizhaobiao.tablestore.nat.entity;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
 * OTS配置
 *
 * @author dunhanson
 * @version 1.0.0
 * @since 2023-07-20
 */
@Data
public class Config {
    /**
     * 实例名称
     */
    @Alias("instance-name")
    private String instanceName;
    /**
     * 端点
     */
    @Alias("end-point")
    private String endPoint;
    /**
     * 账号ID
     */
    @Alias("access-key-id")
    private String accessKeyId;
    /**
     * 账号密码
     */
    @Alias("access-key-secret")
    private String accessKeySecret;
    /**
     * 默认实例
     */
    @Alias("instance-default")
    private Boolean instanceDefault;
    /**
     * 连接超时时间
     */
    @Alias("connection-timeout-in-millisecond")
    private Integer connectionTimeoutInMillisecond;
    /**
     * socket超时时间
     */
    @Alias("socket-timeout-in-millisecond")
    private Integer socketTimeoutInMillisecond;
    /**
     * SearchRequest超时时间
     */
    @Alias("timeout-in-millisecond")
    private Integer timeoutInMillisecond;
    /**
     * Future超时时间
     */
    @Alias("sync-client-wait-future-timeout-in-millis")
    private Long syncClientWaitFutureTimeoutInMillis;
}
