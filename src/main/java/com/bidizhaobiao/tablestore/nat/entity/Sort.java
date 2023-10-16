package com.bidizhaobiao.tablestore.nat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 排序
 * @author dunhanson
 * @since 2023-05.24
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
public class Sort {
    private String field;
    private Order order;
}
