package io.github.harvies.proxypool.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author harvies
 */
@Data
@Builder
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 总记录数
     */
    private long total;
    /**
     * 列表数据
     */
    private List<T> rows;

    public static <T> PageResult<T> of(List<T> list, long total) {
        return PageResult.<T>builder().rows(list).total(total).build();
    }
}
