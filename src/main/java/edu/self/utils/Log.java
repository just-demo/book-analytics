package edu.self.utils;

import java.util.Date;

public class Log {
    public static void time(Date start) {
        System.out.println("time: " + ((new Date().getTime() - start.getTime()) / 1000.) + "sec.");
    }

    public static void memory() {
        long total = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        long free = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        long used = total - free;
        System.out.println("Memory=" + used + "/" + free + "/" + total);
    }
}
