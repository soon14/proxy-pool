package io.github.harvies.proxypool.query;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;

/**
 * @author harvies
 */
@Data
public class PageRequest implements Serializable {

    private static long serialVersionUID = 1L;

    @ApiParam(value = "偏移量", example = "0")
    private int offset = 0;

    @ApiParam(value = "每页条数", example = "20")
    private int limit = 20;
}
