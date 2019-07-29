package io.github.harvies.proxypool.web.query;

import io.swagger.annotations.ApiParam;
import lombok.*;

/**
 * 代理搜索条件
 *
 * @author harvies
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProxyQuery extends PageRequest {
    /**
     * 稳定性大于
     */
    @ApiParam(value = "稳定性大于", example = "5")
    private Double stabilityGt;
    /**
     * 延迟小于等于
     */
    @ApiParam(value = "延迟小于等于", example = "2000")
    private Integer delayLte;

    /**
     * 协议
     */
    @ApiParam(value = "协议", example = "http")
    private String protocol;

    /**
     * 国家代码
     */
    @ApiParam(value = "国家代码", example = "CN")
    private String countryCode;

    /**
     * 能否上google
     */
    @ApiParam(value = "能否上google", example = "true")
    private Boolean google;

    /**
     * 来源
     */
    @ApiParam(value = "来源", example = "1")
    private Integer origin;
    /**
     * ip
     */
    @ApiParam(value = "ip", example = "127.0.0.1")
    private String ip;

    /**
     * 端口
     */
    @ApiParam(value = "端口", example = "1080")
    private Integer port;
}
