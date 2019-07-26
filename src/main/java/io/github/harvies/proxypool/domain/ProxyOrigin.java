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
public class ProxyOrigin {
    private Integer id;
    private String name;

    public static ProxyOrigin of(Integer id, String name) {
        return ProxyOrigin.builder().id(id).name(name).build();
    }
}
