package io.github.harvies.proxypool.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author harvies
 */
public class ProtocolUtils {
    public static String getProtocol(String origin) {
        if (StringUtils.containsIgnoreCase(origin, "https")) {
            return "https";
        } else if (StringUtils.containsIgnoreCase(origin, "socks4")) {
            return "socks4";
        } else if (StringUtils.containsIgnoreCase(origin, "socks5")) {
            return "socks5";
        } else if (StringUtils.containsIgnoreCase(origin, "socks")) {
            return "socks4";
        } else if (StringUtils.containsIgnoreCase(origin, "http")) {
            return "http";
        }
        return null;
    }
}
