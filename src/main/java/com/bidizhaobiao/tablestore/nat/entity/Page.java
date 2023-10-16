package com.bidizhaobiao.tablestore.nat.entity;

import cn.hutool.core.util.PageUtil;
import lombok.Data;
import java.util.List;

/**
 * 分页对象
 * @author dunhanson
 * @since 2023-06-14
 * @version 1.0.0
 */
@Data
public class Page<T> {
    /**
     * 限制大小
     */
    private Integer pageNo;
    /**
     * 偏移量
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer pages;
    /**
     * 总记录数
     */
    private Integer total;
    /**
     * 数据集合
     */
    private List<T> data;

    public Page(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public Page() {
        this.init();
    }

    public void init() {
        if(this.pageNo == null) {
            this.pageNo = 1;
        }
        if(pageSize == null) {
            pageSize = 30;
        }
    }

    public Page(Integer pageNo, Integer pageSize, Integer total, List<T> data) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
        this.data = data;
        this.pages = PageUtil.totalPage(total, this.pageSize);
        this.init();
    }
}
