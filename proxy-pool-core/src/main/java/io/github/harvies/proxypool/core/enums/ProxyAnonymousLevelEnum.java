package io.github.harvies.proxypool.core.enums;

import lombok.Getter;

/**
 * 匿名级别
 *
 * @author harvies
 */

public enum ProxyAnonymousLevelEnum {
    LOW("透明", "low"),

    HIGH("高匿","high");

    @Getter
    private String name;
    @Getter
    private String value;

    ProxyAnonymousLevelEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
