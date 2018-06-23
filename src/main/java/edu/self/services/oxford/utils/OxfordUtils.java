package edu.self.services.oxford.utils;

public class OxfordUtils {
    public static String getFileSubPath(int i) {
        int f1 = (i / 10000) % 100;
        int f2 = (i / 100) % 100;
        return f1 + "\\" + f2;
    }
}
