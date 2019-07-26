
package io.github.harvies.proxypool.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author harvies
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProxyResult {

    private String as;
    private String city;
    private String country;
    private String countryCode;
    private String isp;
    private Double lat;
    private Double lon;
    private String org;
    private String query;
    private String region;
    private String regionName;
    private String status;
    private String timezone;
    private String zip;
    private Long delay;
    private boolean google;

}
