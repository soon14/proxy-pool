package io.github.harvies.proxypool.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

/**
 * @author harvies
 * @Document 用来表明关联的mongo中的那个collection(类似于表名)
 * @Indexed 为某个字段建立索引
 * @Field 声明属性对应的数据库中的哪个字段
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "proxy")
@CompoundIndexes({
        @CompoundIndex(name = "unique_ip_port", def = "{ 'ip': 1, 'port': 1 }", unique = true)
})
@ApiModel(value = "代理")
public class Proxy implements Serializable {

    private static long serialVersionUID = 1L;

    /**
     * 延迟默认值
     */
    public static int DELAY_DEFAULT = 99999;
    /**
     * 稳定性默认值
     */
    public static int STABILITY_DEFAULT = 50;

    @Id
    private String id;

    @ApiParam(value = "ip", example = "127.0.0.1", required = true)
    private String ip;

    @ApiParam(value = "端口", example = "1080", required = true)
    private Integer port;
    /**
     * 协议 http https socks
     */
    @ApiParam(value = "协议", example = "http", required = true)
    private String protocol;
    /**
     * 匿名级别
     */
    @Field("anonymous_level")
    private String anonymousLevel;
    /**
     * 国家
     */
    private String country;
    /**
     * 国家代码
     */
    @Field("country_code")
    private String countryCode;
    /**
     * 地区
     */
    @Field("region_name")
    private String regionName;
    /**
     * 城市
     */
    private String city;
    /**
     * 延迟
     */
    @Indexed
    private Integer delay;
    /**
     * 稳定性
     */
    private Integer stability;

    /**
     * 检测连续失败数
     */
    @Field("check_continuous_failed_num")
    private Integer checkContinuousFailedNum;

    /**
     * 经度
     */
    private Double lon;
    /**
     * 纬度
     */
    private Double lat;

    private String isp;

    /**
     * 来源
     */
    private Integer origin;

    /**
     * 能否上Google
     */
    private boolean google;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Field("gmt_create")
    private Date gmtCreate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Field("gmt_modified")
    private Date gmtModified;

    @JsonIgnore
    @ApiParam(hidden = true)
    public java.net.Proxy.Type getType() {
        if (protocol.toLowerCase().contains("http")) {
            return java.net.Proxy.Type.HTTP;
        } else if (protocol.toLowerCase().contains("socks")) {
            return java.net.Proxy.Type.SOCKS;
        }
        return null;
    }

}
