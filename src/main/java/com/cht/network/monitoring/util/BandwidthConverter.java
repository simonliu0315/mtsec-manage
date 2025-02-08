package com.cht.network.monitoring.util;

public class BandwidthConverter {
    private static final long MEGABIT = 1000000L;
    private static final long GIGABIT = 1000000000L;

    public static String bandwidth(String bandwidthStr) {
        // 将字符串转换为 long 型数字
        long bandwidth = Long.parseLong(bandwidthStr);

        // 判断并格式化输出
        if (bandwidth >= GIGABIT) {
            return String.format("%.1f Gbps", (double) bandwidth / GIGABIT);
        } else if (bandwidth >= MEGABIT) {
            return String.format("%.1f Mbps", (double) bandwidth / MEGABIT);
        } else {
            return bandwidth + " bps";
        }
    }

}
