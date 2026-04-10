package com.mamba.common.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果包装类
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 数据列表
     */
    private List<T> data;

    /**
     * 当前页码
     */
    private int pageNum;

    /**
     * 每页大小
     */
    private int pageSize;

    public PageResult(long total, List<T> data, int pageNum, int pageSize) {
        this.total = total;
        this.data = data;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    /**
     * 将 MyBatis-Plus 的 IPage 转换为 PageResult
     */
    public static <T> PageResult<T> of(IPage<T> page) {
        return new PageResult<>(
                page.getTotal(),
                page.getRecords(),
                (int) page.getCurrent(),
                (int) page.getSize()
        );
    }

}
